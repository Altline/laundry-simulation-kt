package altline.appliance.ui.mapper

import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.substance.Substance
import altline.appliance.ui.component.washerInfo.*
import altline.appliance.ui.component.washerInfo.CyclePhaseUi.ActiveState.*
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.util.clockFormat
import altline.appliance.ui.util.optionalDecimal
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.PhaseStatus
import altline.appliance.washing.laundry.washCycle.SectionStatus
import altline.appliance.washing.laundry.washCycle.SpinParams
import altline.appliance.washing.laundry.washCycle.TumbleParams
import altline.appliance.washing.laundry.washCycle.phase.DrainPhase
import altline.appliance.washing.laundry.washCycle.phase.FillPhase
import altline.appliance.washing.laundry.washCycle.phase.SpinPhase
import altline.appliance.washing.laundry.washCycle.phase.WashPhase
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class WasherInfoMapper(
    private val washerMapper: WasherMapper,
    private val stringMapper: StringMapper
) {

    fun mapToInfoPanel(
        washer: StandardLaundryWasherBase,
        speedSettings: List<Float>,
        selectedSpeed: Float,
        onSpeedClick: (Float) -> Unit
    ): InfoPanelUi {
        return InfoPanelUi(
            cycleSectionUi = mapToParamsSection(washer),
            washerStateSectionUi = mapToWasherStateSection(washer),
            simulationControlPanelUi = SimulationControlPanelUi(
                speedSettings = speedSettings,
                selectedSpeed = selectedSpeed,
                onSpeedClick = onSpeedClick
            )
        )
    }

    private fun mapToParamsSection(washer: StandardLaundryWasherBase): WashCycleSectionUi {
        with(washer) {
            val duration = selectedCycle.duration.clockFormat()
            val timer = buildString {
                if (selectedCycle == activeCycle) {
                    runningTime?.let {
                        append("${it.clockFormat()} / ")
                    }
                }
                append(duration)
            }
            return WashCycleSectionUi(
                cycleName = washerMapper.mapToWashCycleName(selectedCycle),
                timer = timer,
                stages = washer.selectedCycleStatus.map { stage ->
                    CycleStageUi(
                        phases = mapPhases(stage.phases)
                    )
                }
            )
        }
    }

    private fun mapPhases(phases: List<PhaseStatus>): List<CyclePhaseUi> {
        return phases.map { phaseStatus ->
            val phase = phaseStatus.phase
            CyclePhaseUi(
                name = stringMapper.mapPhaseName(phase),
                timer = mapToTimer(phaseStatus.runningTime, phase.duration),
                sections = mapSections(phaseStatus.sections, phaseStatus),
                active = mapToPhaseActiveState(phases, phaseStatus),
                disabled = (phase as? SpinPhase)?.disabled == true
            )
        }
    }

    private fun mapSections(sections: List<SectionStatus>, phaseStatus: PhaseStatus): List<PhaseSectionUi> {
        return sections.mapIndexedNotNull { index, sectionStatus ->
            val indexInPhase = phaseStatus.sections.indexOf(sectionStatus)
            val phase = phaseStatus.phase

            when (val section = sectionStatus.section) {
                is FillPhase.Section -> {
                    // If there is more than one fill section in a fill phase, then show them,
                    // otherwise collapse the section into the phase.
                    if (sections.getOrNull(index - 1) is FillPhase.Section ||
                        sections.getOrNull(index + 1) is FillPhase.Section
                    ) {
                        PhaseSectionUi(
                            name = stringMapper.mapFillSectionName(section),
                            timer = mapToTimer(sectionStatus.runningTime, section.duration),
                            params = null,
                            active = sectionStatus.active
                        )
                    } else null
                }

                is WashPhase.Section -> {
                    PhaseSectionUi(
                        name = "${strings["cyclePhaseSection"]} ${indexInPhase + 1}",
                        timer = mapToTimer(sectionStatus.runningTime, section.duration),
                        params = mapTumbleParams(section.params),
                        active = sectionStatus.active
                    )
                }

                is DrainPhase.Section.FocusedDrain -> {
                    PhaseSectionUi(
                        name = strings["cyclePhaseSection_fullFlow"],
                        timer = mapToTimer(sectionStatus.runningTime, section.duration),
                        params = null,
                        active = sectionStatus.active
                    )
                }

                is DrainPhase.Section.TumbleDrain -> {
                    PhaseSectionUi(
                        name = strings["cyclePhaseSection_washDrain"],
                        timer = mapToTimer(sectionStatus.runningTime, section.duration),
                        params = mapTumbleParams(section.tumbleParams),
                        active = sectionStatus.active
                    )
                }

                is SpinPhase.Section -> {
                    if ((phase as? SpinPhase)?.disabled == true) null
                    else {
                        PhaseSectionUi(
                            name = "${strings["cyclePhaseSection"]} ${indexInPhase + 1}",
                            timer = mapToTimer(sectionStatus.runningTime, section.duration),
                            params = mapSpinParams(section.params),
                            active = sectionStatus.active
                        )
                    }
                }

                else -> null
            }
        }
    }

    private fun mapToTimer(runningTime: Measure<Time>, duration: Measure<Time>? = null): String {
        val durationString = duration?.clockFormat() ?: "-"
        return if (runningTime == 0 * seconds) durationString
        else "${runningTime.clockFormat()} / $durationString"
    }

    private fun mapToPhaseActiveState(allPhases: List<PhaseStatus>, phase: PhaseStatus): CyclePhaseUi.ActiveState {
        if (phase.active) return ACTIVE

        val phaseIndex = allPhases.indexOf(phase)
        val indexOfActive = allPhases.indexOfFirst { it.active }
        return if (phaseIndex < indexOfActive) EXECUTED
        else NOT_EXECUTED
    }

    private fun mapTumbleParams(tumbleParams: TumbleParams): List<SectionParamUi> {
        return listOf(
            SectionParamUi(
                name = strings["washParams_spinPeriod"],
                value = tumbleParams.spinPeriod.optionalDecimal()
            ),
            SectionParamUi(
                name = strings["washParams_restPeriod"],
                value = tumbleParams.restPeriod.optionalDecimal()
            ),
            SectionParamUi(
                name = strings["washParams_spinSpeed"],
                value = tumbleParams.spinSpeed.optionalDecimal()
            )
        ).run {
            tumbleParams.temperature?.let {
                plus(
                    SectionParamUi(
                        name = strings["washParams_temperature"],
                        value = it.optionalDecimal()
                    )
                )
            } ?: this
        }
    }

    private fun mapSpinParams(spinParams: SpinParams): List<SectionParamUi> {
        return listOf(
            SectionParamUi(
                name = strings["washParams_spinSpeed"],
                value = spinParams.spinSpeed.optionalDecimal()
            ),
            SectionParamUi(
                name = strings["washParams_duration"],
                value = spinParams.duration.optionalDecimal()
            )
        )
    }

    private fun mapToWasherStateSection(washer: StandardLaundryWasherBase): WasherStateSectionUi? {
        return washer.scanner?.run {
            WasherStateSectionUi(
                spinSpeed = spinSpeed,
                temperature = washLiquid?.temperature?.optionalDecimal(maxFractionDigits = 1) ?: "-",
                liquidLevel = washLiquid?.amount?.optionalDecimal(mandatoryUnits = liters) ?: "-",
                liquidParts = mapSubstanceParts(washLiquid)
            )
        }
    }

    private fun mapSubstanceParts(substance: Substance?): List<Pair<String, String>> {
        return substance?.parts
            ?.sortedByDescending { it.amount }
            ?.map { part ->
                Pair(
                    stringMapper.mapSubstanceTypeName(part.type),
                    part.amount.optionalDecimal()
                )
            } ?: emptyList()
    }
}
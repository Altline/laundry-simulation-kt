package altline.appliance.ui.mapper

import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.ui.component.washerInfo.*
import altline.appliance.ui.component.washerInfo.CyclePhaseUi.ActiveState.*
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.util.clockFormat
import altline.appliance.ui.util.optionalDecimal
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashParams
import altline.appliance.washing.laundry.washCycle.phase.*
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.Time.Companion.seconds
import io.nacular.measured.units.times

class WasherInfoMapper(
    private val washerMapper: WasherMapper
) {

    fun mapToInfoPanel(washer: StandardLaundryWasherBase): InfoPanelUi {
        return InfoPanelUi(
            cycleSectionUi = mapToParamsSection(washer),
            washerStateSectionUi = mapToWasherStateSection(washer)
        )
    }

    private fun mapToParamsSection(washer: StandardLaundryWasherBase): WashCycleSectionUi {
        with(washer) {
            val duration = selectedWashCycle.estimatedDuration.clockFormat()
            val timer = buildString {
                if (selectedWashCycle == activeWashCycle) {
                    runningTime?.let {
                        append("${it.clockFormat()} / ")
                    }
                }
                append(duration)
            }
            return WashCycleSectionUi(
                cycleName = washerMapper.mapToWashCycleName(selectedWashCycle),
                timer = timer,
                phases = mapPhases(selectedWashCycle.stages.flatMap { it.phases })
            )
        }
    }

    private fun mapPhases(phases: List<CyclePhase>): List<CyclePhaseUi> {
        return phases.map { phase ->
            CyclePhaseUi(
                name = mapToPhaseName(phase),
                timer = mapToTimer(phase.runningTime, phase.duration),
                sections = when (phase) {
                    is WashPhase -> {
                        phase.sections.mapIndexed { index, section ->
                            PhaseSectionUi(
                                name = "${strings["cyclePhaseSection"]} ${index + 1}",
                                timer = mapToTimer(section.runningTime, section.duration),
                                params = mapWashParams(section.washParams),
                                active = section.active
                            )
                        }
                    }

                    is DrainPhase -> {
                        phase.sections.map { section ->
                            val name: String
                            val timer: String
                            val params: WashParamsUi?
                            when (section) {
                                is DrainPhase.FocusedDrainSection -> {
                                    name = strings["cyclePhaseSection_fullFlow"]
                                    timer = mapToTimer(section.runningTime)
                                    params = null
                                }

                                is DrainPhase.WashDrainSection -> {
                                    name = strings["cyclePhaseSection_washDrain"]
                                    timer = mapToTimer(section.runningTime, section.spinParams.duration)
                                    params = mapWashParams(section.spinParams)
                                }
                            }
                            PhaseSectionUi(
                                name = name,
                                timer = timer,
                                params = params,
                                active = section.active
                            )
                        }
                    }

                    else -> emptyList()
                },
                active = mapToPhaseActiveState(phases, phase)
            )
        }
    }

    private fun mapWashParams(washParams: WashParams): WashParamsUi {
        return WashParamsUi(
            spinPeriod = washParams.spinPeriod.optionalDecimal(),
            restPeriod = washParams.restPeriod.optionalDecimal(),
            spinSpeed = washParams.spinSpeed.optionalDecimal()
        )
    }

    private fun mapToPhaseName(phase: CyclePhase): String {
        return buildString {
            append(
                when (phase) {
                    is FillPhase -> strings["cyclePhase_fill"]
                    is WashPhase -> strings["cyclePhase_wash"]
                    is DrainPhase -> strings["cyclePhase_drain"]
                    is SpinPhase -> strings["cyclePhase_spin"]
                    else -> ""
                }
            )
            if (phase is FillPhase) {
                append(" (")
                append(
                    when (phase) {
                        is DetergentFillPhase -> strings["cyclePhase_fill_detergent"]
                        is SoftenerFillPhase -> strings["cyclePhase_fill_softener"]
                        else -> ""
                    }
                )
                append("; ${phase.fillToAmount.optionalDecimal()})")
            }
            if (phase is SpinPhase) {
                append(" (${phase.params.spinSpeed.optionalDecimal()})")
            }
        }
    }

    private fun mapToPhaseActiveState(allPhases: List<CyclePhase>, phase: CyclePhase): CyclePhaseUi.ActiveState {
        if (phase.active) return ACTIVE

        val phaseIndex = allPhases.indexOf(phase)
        val indexOfActive = allPhases.indexOfFirst { it.active }
        return if (phaseIndex < indexOfActive) EXECUTED
        else NOT_EXECUTED
    }

    private fun mapToTimer(runningTime: Measure<Time>, duration: Measure<Time>? = null): String {
        val durationString = duration?.clockFormat() ?: "-"
        return if (runningTime == 0 * seconds) durationString
        else "${runningTime.clockFormat()} / $durationString"
    }

    private fun mapToWasherStateSection(washer: StandardLaundryWasherBase): WasherStateSectionUi? {
        return washer.scanner?.run {
            WasherStateSectionUi(
                spinSpeed = spinSpeed,
                temperature = temperature?.optionalDecimal(maxFractionDigits = 1) ?: "-",
                waterLevel = waterLevel.optionalDecimal(mandatoryUnits = liters)
            )
        }
    }
}
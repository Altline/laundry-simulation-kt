package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.washCycle.phase.CyclePhase
import altline.appliance.washing.laundry.washCycle.phase.PhaseSection
import io.nacular.measured.units.*

class StageStatus(
    val stage: CycleStage,
    val phases: List<PhaseStatus>
) {
    val runningTime: Measure<Time>
        get() = phases.sumOf { it.runningTime }
}

class PhaseStatus(
    val phase: CyclePhase,
    val sections: List<SectionStatus>
) {
    val active: Boolean
        get() = sections.any { it.active }

    val runningTime: Measure<Time>
        get() = sections.sumOf { it.runningTime }
}

interface SectionStatus {
    val section: PhaseSection
    val active: Boolean
    val runningTime: Measure<Time>
}
package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.phase.CyclePhase
import io.nacular.measured.units.*

class CycleStage(
    val phases: List<CyclePhase>
) {

    val estimatedDuration: Measure<Time>
        get() = phases.sumOf { it.duration }

    suspend fun execute(washer: StandardLaundryWasherBase) {
        phases.forEach { it.execute(washer) }
    }
}
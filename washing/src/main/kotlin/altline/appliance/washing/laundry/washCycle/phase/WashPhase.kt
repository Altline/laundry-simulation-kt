package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*

class WashPhase(
    val sections: List<WashParams>
) : CyclePhase {

    override val duration: Measure<Time>
        get() = sections.sumOf { it.washDuration }

    override suspend fun execute(washer: StandardLaundryWasherBase) {
        sections.forEach { params ->
            washer.wash(params)
        }
    }
}
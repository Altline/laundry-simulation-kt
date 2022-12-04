package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Volume
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.standardIntakeFlowRate
import io.nacular.measured.units.*

class DetergentFillPhase(
    val fillToAmount: Measure<Volume>
) : CyclePhase {

    override val duration: Measure<Time>
        get() = fillToAmount / standardIntakeFlowRate

    override suspend fun execute(washer: StandardLaundryWasherBase) {
        washer.fillThroughDetergent(fillToAmount)
    }
}
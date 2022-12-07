package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Volume
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*

class DetergentFillPhase(fillToAmount: Measure<Volume>) : FillPhase(fillToAmount) {

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        washer.fillThroughDetergent(fillToAmount)
    }
}
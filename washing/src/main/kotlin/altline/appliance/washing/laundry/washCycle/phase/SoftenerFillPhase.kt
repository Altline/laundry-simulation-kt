package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Volume
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*

class SoftenerFillPhase(fillToAmount: Measure<Volume>) : FillPhase(fillToAmount) {

    override suspend fun execute(washer: StandardLaundryWasherBase) {
        washer.fillThroughSoftener(fillToAmount)
    }
}
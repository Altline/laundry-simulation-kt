package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.standardFullFlowDrainTime
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*

class DrainPhase(
    val spinParams: WashParams
) : CyclePhase {

    override val duration: Measure<Time>
        get() = standardFullFlowDrainTime + spinParams.washDuration

    override suspend fun execute(washer: StandardLaundryWasherBase) {
        washer.drain()
        // TODO spin drain
    }
}
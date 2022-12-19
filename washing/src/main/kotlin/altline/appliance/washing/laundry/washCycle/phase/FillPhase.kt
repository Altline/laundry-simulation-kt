package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Volume
import altline.appliance.washing.laundry.StandardIntakeFlowRate
import io.nacular.measured.units.*

abstract class FillPhase(
    val fillToAmount: Measure<Volume>
) : CyclePhaseBase() {

    override val duration: Measure<Time>
        get() = fillToAmount / StandardIntakeFlowRate
}
package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Volume
import altline.appliance.measure.delay
import altline.appliance.washing.laundry.StandardIntakeFlowRate
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

abstract class FillPhase(
    val fillToAmount: Measure<Volume>
) : CyclePhaseBase() {

    private val endDelay = 1 * seconds

    override val duration: Measure<Time>
        get() = fillToAmount / StandardIntakeFlowRate + endDelay

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        executeFill(washer)
        delay(endDelay)
    }

    protected abstract suspend fun executeFill(washer: StandardLaundryWasherBase)
}
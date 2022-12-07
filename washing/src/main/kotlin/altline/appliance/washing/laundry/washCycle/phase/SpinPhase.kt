package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.CentrifugeParams
import io.nacular.measured.units.*

class SpinPhase(
    val params: CentrifugeParams
) : CyclePhaseBase() {

    override val duration: Measure<Time>
        get() = params.duration

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        washer.centrifuge(params)
    }
}
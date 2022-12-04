package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.CentrifugeParams
import io.nacular.measured.units.*

class SpinPhase(
    val params: CentrifugeParams
) : CyclePhase {

    override val duration: Measure<Time>
        get() = params.duration

    override suspend fun execute(washer: StandardLaundryWasherBase) {
        washer.centrifuge(params)
    }
}
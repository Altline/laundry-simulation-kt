package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.CentrifugeParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class SpinPhase(
    val params: CentrifugeParams
) : CyclePhaseBase() {

    override val duration: Measure<Time>
        get() = if (params.duration != 0 * rpm) params.duration else 0 * seconds

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        if (params.duration != 0 * rpm) washer.centrifuge(params)
    }
}
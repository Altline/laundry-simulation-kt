package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*

interface CyclePhase {
    /** Programmed or estimated duration of the phase. The duration is estimated (as opposed to programmed) in cases
     * where it depends on outside factors such as liquid flow rate. */
    val duration: Measure<Time>

    val runningTime: Measure<Time>
    val active: Boolean

    suspend fun execute(washer: StandardLaundryWasherBase)
}
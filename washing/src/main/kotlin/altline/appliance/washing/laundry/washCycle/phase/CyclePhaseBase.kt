package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.common.RefreshPeriod
import altline.appliance.common.TimeFactor
import altline.appliance.measure.repeatPeriodically
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashCycleDsl
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@WashCycleDsl
abstract class CyclePhaseBase : CyclePhase {

    final override var runningTime: Measure<Time> = 0 * seconds
        private set

    final override var active: Boolean = false
        private set

    final override suspend fun execute(washer: StandardLaundryWasherBase) {
        coroutineScope {
            val timerJob = launch {
                repeatPeriodically(RefreshPeriod) {
                    runningTime += (TimeFactor `in` seconds) * seconds
                }
            }
            active = true
            doExecute(washer)
            active = false
            timerJob.cancel()
        }
    }

    protected abstract suspend fun doExecute(washer: StandardLaundryWasherBase)
}
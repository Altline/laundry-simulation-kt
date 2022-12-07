package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.repeatPeriodically
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class CyclePhaseBase : CyclePhase {

    final override var runningTime: Measure<Time> = 0 * Time.seconds
        private set

    final override var active: Boolean = false
        private set

    final override suspend fun execute(washer: StandardLaundryWasherBase) {
        coroutineScope {
            launch {
                repeatPeriodically(1 * Time.seconds) {
                    runningTime += 1 * Time.seconds
                }
            }
            active = true
            doExecute(washer)
            active = false
            cancel()
        }
    }

    protected abstract suspend fun doExecute(washer: StandardLaundryWasherBase)
}
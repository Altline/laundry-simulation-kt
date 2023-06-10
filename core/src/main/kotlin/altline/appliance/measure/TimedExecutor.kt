package altline.appliance.measure

import altline.appliance.common.RefreshPeriod
import altline.appliance.common.TimeFactor
import io.nacular.measured.units.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TimedExecutor {
    var runningTime: Measure<Time> = 0 * Time.seconds
        private set

    var active: Boolean = false
        private set

    suspend fun executeTimed(block: suspend () -> Unit) {
        coroutineScope {
            val timerJob = launch {
                repeatPeriodically(RefreshPeriod) {
                    runningTime += (TimeFactor `in` Time.seconds) * Time.seconds
                }
            }
            active = true
            block.invoke()
            active = false
            timerJob.cancel()
        }
    }
}
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

    private val timedWrapper = TimedWrapper()

    final override val runningTime: Measure<Time>
        get() = timedWrapper.runningTime

    final override val active: Boolean
        get() = timedWrapper.active

    final override suspend fun execute(washer: StandardLaundryWasherBase) {
        timedWrapper.executeTimed { doExecute(washer) }
    }

    protected abstract suspend fun doExecute(washer: StandardLaundryWasherBase)

    override fun reset() {
        timedWrapper.reset()
    }

    class TimedWrapper {
        var runningTime: Measure<Time> = 0 * seconds
            private set

        var active: Boolean = false
            private set

        suspend fun executeTimed(block: suspend () -> Unit) {
            coroutineScope {
                val timerJob = launch {
                    repeatPeriodically(RefreshPeriod) {
                        runningTime += (TimeFactor `in` seconds) * seconds
                    }
                }
                active = true
                block.invoke()
                active = false
                timerJob.cancel()
            }
        }

        fun reset() {
            runningTime = 0 * seconds
            active = false
        }
    }
}
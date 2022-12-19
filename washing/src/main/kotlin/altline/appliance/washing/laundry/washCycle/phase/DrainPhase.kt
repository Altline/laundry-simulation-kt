package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.common.RefreshPeriod
import altline.appliance.common.TimeFactor
import altline.appliance.measure.repeatPeriodically
import altline.appliance.washing.laundry.StandardFullFlowDrainTime
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DrainPhase(
    val spinParams: WashParams
) : CyclePhaseBase() {

    val sections = listOf(
        FocusedDrainSection(),
        WashDrainSection(spinParams)
    )

    override val duration: Measure<Time>
        get() = standardFullFlowDrainTime + spinParams.duration

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        sections.forEach { it.execute(washer) }
    }

    sealed class Section {
        var runningTime: Measure<Time> = 0 * seconds
            private set

        var active: Boolean = false
            private set

        suspend fun execute(washer: StandardLaundryWasherBase) {
            coroutineScope {
                launch {
                    repeatPeriodically(RefreshPeriod) {
                        runningTime += (TimeFactor `in` seconds) * seconds
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

    class FocusedDrainSection : Section() {
        override suspend fun doExecute(washer: StandardLaundryWasherBase) {
            washer.drain()
        }
    }

    class WashDrainSection(val spinParams: WashParams) : Section() {
        override suspend fun doExecute(washer: StandardLaundryWasherBase) {
            // TODO
        }
    }
}
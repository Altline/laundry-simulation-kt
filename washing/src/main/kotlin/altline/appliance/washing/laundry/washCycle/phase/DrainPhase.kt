package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.delay
import altline.appliance.washing.laundry.StandardFullFlowDrainTime
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class DrainPhase(
    val spinParams: WashParams
) : CyclePhaseBase() {

    private val endDelay = 2 * seconds

    val sections = listOf(
        FocusedDrainSection(),
        WashDrainSection(spinParams)
    )

    override val duration: Measure<Time>
        get() = StandardFullFlowDrainTime + spinParams.duration + endDelay

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        sections.forEach { it.execute(washer) }
        delay(endDelay)
    }

    sealed class Section {
        private val timedWrapper = TimedWrapper()

        val runningTime: Measure<Time>
            get() = timedWrapper.runningTime

        val active: Boolean
            get() = timedWrapper.active

        suspend fun execute(washer: StandardLaundryWasherBase) {
            timedWrapper.executeTimed { doExecute(washer) }
        }

        protected abstract suspend fun doExecute(washer: StandardLaundryWasherBase)
    }

    class FocusedDrainSection : Section() {
        override suspend fun doExecute(washer: StandardLaundryWasherBase) {
            washer.drainUntilEmpty()
        }
    }

    class WashDrainSection(val spinParams: WashParams) : Section() {
        override suspend fun doExecute(washer: StandardLaundryWasherBase) {
            with(washer) {
                startDrain()
                wash(spinParams)
                stopDrain()
            }
        }
    }
}
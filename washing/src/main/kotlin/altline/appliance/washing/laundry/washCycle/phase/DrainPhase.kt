package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.washing.laundry.StandardFullFlowDrainTime
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time

class DrainPhase(
    val spinParams: WashParams
) : CyclePhaseBase() {

    val sections = listOf(
        FocusedDrainSection(),
        WashDrainSection(spinParams)
    )

    override val duration: Measure<Time>
        get() = StandardFullFlowDrainTime + spinParams.duration

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        sections.forEach { it.execute(washer) }
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
            washer.drain()
        }
    }

    class WashDrainSection(val spinParams: WashParams) : Section() {
        override suspend fun doExecute(washer: StandardLaundryWasherBase) {
            // TODO
        }
    }
}
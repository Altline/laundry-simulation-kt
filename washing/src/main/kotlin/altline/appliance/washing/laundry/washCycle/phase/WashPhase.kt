package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.repeatPeriodically
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class WashPhase(
    vararg val sections: Section
) : CyclePhaseBase() {

    override val duration: Measure<Time>
        get() = sections.sumOf { it.duration }

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        sections.forEach { it.execute(washer) }
    }

    class Section(
        val washParams: WashParams
    ) {
        val duration: Measure<Time>
            get() = washParams.duration

        var runningTime: Measure<Time> = 0 * seconds
            private set

        var active: Boolean = false
            private set

        suspend fun execute(washer: StandardLaundryWasherBase) {
            coroutineScope {
                launch {
                    repeatPeriodically(1 * seconds) {
                        runningTime += 1 * seconds
                    }
                }
                active = true
                washer.wash(washParams)
                active = false
                cancel()
            }
        }
    }
}
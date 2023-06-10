package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardFullFlowDrainTime
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class DrainPhase(spinParams: WashParams) : CyclePhase {

    override val sections = listOf(
        Section.FocusedDrain,
        Section.WashDrain(spinParams)
    )

    override val duration: Measure<Time>
        get() = sections.sumOf { it.duration }

    sealed class Section(duration: Measure<Time>) : PhaseSection {
        final override val endDelay: Measure<Time> = 0 * seconds
        final override val duration: Measure<Time> = duration + endDelay

        object FocusedDrain : Section(StandardFullFlowDrainTime)
        data class WashDrain(val spinParams: WashParams) : Section(spinParams.duration)
    }
}
package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardFullFlowDrainTime
import altline.appliance.washing.laundry.washCycle.TumbleParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class DrainPhase(tumbleParams: TumbleParams) : CyclePhase {

    override val sections = listOf(
        Section.FocusedDrain,
        Section.TumbleDrain(tumbleParams)
    )

    override val duration: Measure<Time>
        get() = sections.sumOf { it.duration }

    sealed class Section(duration: Measure<Time>) : PhaseSection {
        final override val endDelay: Measure<Time> = 0 * seconds
        final override val duration: Measure<Time> = duration + endDelay

        object FocusedDrain : Section(StandardFullFlowDrainTime)
        data class TumbleDrain(val tumbleParams: TumbleParams) : Section(tumbleParams.duration)
    }
}
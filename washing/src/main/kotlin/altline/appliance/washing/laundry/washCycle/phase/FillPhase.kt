package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Volume
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardIntakeFlowRate
import altline.appliance.washing.laundry.washCycle.WashCycleDsl
import altline.appliance.washing.laundry.washCycle.phase.DrainPhase.Section.FocusedDrain.endDelay
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

@WashCycleDsl
class FillPhase : CyclePhase {

    override var sections: List<Section> = emptyList()
        private set

    override val duration: Measure<Time>
        get() = sections.sumOf { it.duration } + endDelay

    fun detergentFillSection(fillToAmount: Measure<Volume>): DetergentFillSection {
        return DetergentFillSection(fillToAmount).also {
            sections += it
        }
    }

    fun softenerFillSection(fillToAmount: Measure<Volume>): SoftenerFillSection {
        return SoftenerFillSection(fillToAmount).also {
            sections += it
        }
    }

    data class DetergentFillSection(override val fillToAmount: Measure<Volume>) : Section(fillToAmount)
    data class SoftenerFillSection(override val fillToAmount: Measure<Volume>) : Section(fillToAmount)

    abstract class Section(open val fillToAmount: Measure<Volume>) : PhaseSection {
        override val endDelay = 1 * seconds

        override val duration: Measure<Time>
            get() = fillToAmount / StandardIntakeFlowRate + endDelay
    }
}
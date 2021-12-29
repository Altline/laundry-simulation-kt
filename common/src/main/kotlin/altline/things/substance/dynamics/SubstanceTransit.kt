package altline.things.substance.dynamics

import altline.things.measure.Volume
import altline.things.substance.MutableSubstance
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.UnitsRatio

interface SubstanceSource {
    val maxFlowRate: Measure<UnitsRatio<Volume, Time>>
    val drain: SubstanceDrain?

    fun connectDrain(drain: SubstanceDrain)
    fun disconnectDrain()

    fun pull(amount: Measure<Volume>, timeFrame: Measure<Time>, flowId: Long): MutableSubstance
}

interface SubstanceDrain {
    val maxFlowRate: Measure<UnitsRatio<Volume, Time>>
    val source: SubstanceSource?

    fun connectSource(source: SubstanceSource)
    fun disconnectSource()

    fun push(substance: MutableSubstance, timeFrame: Measure<Time>, flowId: Long): Measure<Volume>
}

interface SubstanceConduit : SubstanceSource, SubstanceDrain
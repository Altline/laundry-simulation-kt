package altline.things.substances

import altline.things.measure.Volume
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.UnitsRatio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel

interface SubstanceSource {
    val maxFlowRate: Measure<UnitsRatio<Volume, Time>>

    fun collect(
        amount: Measure<Volume>,
        flowRate: Measure<UnitsRatio<Volume, Time>> = maxFlowRate,
        scope: CoroutineScope
    ): ReceiveChannel<MutableSubstance>
}

interface SubstanceDrain {
    val maxFlowRate: Measure<UnitsRatio<Volume, Time>>

    suspend fun emit(amount: MutableSubstance)
}

interface SubstanceConduit : SubstanceSource, SubstanceDrain
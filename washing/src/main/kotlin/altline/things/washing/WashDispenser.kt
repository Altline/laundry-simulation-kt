package altline.things.washing

import altline.things.measure.Volume
import altline.things.substance.MutableSubstance
import altline.things.substance.dynamics.SubstanceSource
import io.nacular.measured.units.Measure
import kotlinx.coroutines.CoroutineScope

interface WashDispenser {
    fun connectSolventSource(conduit: SubstanceSource)

    fun dispenseMainDetergent(solventAmount: Measure<Volume>, scope: CoroutineScope)
            : kotlinx.coroutines.channels.ReceiveChannel<MutableSubstance>

    interface Channel {
        fun dispense(solventAmount: Measure<Volume>, scope: CoroutineScope)
                : kotlinx.coroutines.channels.ReceiveChannel<MutableSubstance>
    }
}
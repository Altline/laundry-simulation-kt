package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.measure.Spin
import altline.things.measure.Spin.Companion.rpm
import altline.things.measure.Volume
import altline.things.substance.MutableSubstance
import altline.things.substance.Substance
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.times
import kotlinx.coroutines.channels.ReceiveChannel

interface Drum {
    val capacity: Measure<Volume>
    val excessLiquidAmount: Measure<Volume>

    fun load(item: Body)
    fun load(items: Collection<Body>)
    fun unload(item: Body)
    fun unload(items: Collection<Body>)
    fun unload(): List<Body>
    suspend fun fill(washLiquidFlow: ReceiveChannel<Substance>)
    fun drain(): ReceiveChannel<MutableSubstance>
    suspend fun spin(speed: Measure<Spin>, duration: Measure<Time>)

    companion object {
        internal val CentrifugeThreshold = 80 * rpm

        /** The lower soak ratio limit where washing can take place */
        internal const val LowerSoakRatio = 0.75

        /** The upper soak ratio limit where washing effectiveness no longer depends on soakness. */
        internal const val UpperSoakRatio = 1.25
    }
}
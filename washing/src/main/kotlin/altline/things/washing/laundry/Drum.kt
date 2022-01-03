package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.measure.Spin
import altline.things.measure.Volume
import altline.things.substance.Substance
import io.nacular.measured.units.*
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
    fun drain(): ReceiveChannel<Substance>
    suspend fun spin(speed: Measure<Spin>, duration: Measure<Time>)
}
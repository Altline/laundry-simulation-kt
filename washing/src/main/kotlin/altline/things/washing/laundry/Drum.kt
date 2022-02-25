package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.measure.Spin
import altline.things.measure.Volume
import altline.things.substance.transit.SubstanceDrainPort
import altline.things.substance.transit.SubstanceSourcePort
import io.nacular.measured.units.*

interface Drum {
    val capacity: Measure<Volume>
    val excessLiquidAmount: Measure<Volume>

    val inputPort: SubstanceDrainPort
    val outputPort: SubstanceSourcePort

    fun load(item: Body)
    fun load(items: Collection<Body>)
    fun unload(item: Body)
    fun unload(items: Collection<Body>)
    fun unload(): List<Body>

    suspend fun spin(speed: Measure<Spin>, duration: Measure<Time>)
}
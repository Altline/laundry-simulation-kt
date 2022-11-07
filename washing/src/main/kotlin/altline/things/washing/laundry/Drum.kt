package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.electricity.ElectricHeater
import altline.things.measure.Volume
import altline.things.spin.Spinnable
import altline.things.substance.transit.SubstanceDrainPort
import altline.things.substance.transit.SubstanceSourcePort
import io.nacular.measured.units.*

interface Drum : Spinnable {
    val capacity: Measure<Volume>
    val excessLiquidAmount: Measure<Volume>
    val heater: ElectricHeater

    val inputPort: SubstanceDrainPort
    val outputPort: SubstanceSourcePort

    fun load(vararg items: Body)
    fun unload(vararg items: Body)
    fun unloadAll(): List<Body>
}
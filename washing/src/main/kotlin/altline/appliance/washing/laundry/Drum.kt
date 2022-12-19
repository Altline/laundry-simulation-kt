package altline.appliance.washing.laundry

import altline.appliance.common.Body
import altline.appliance.electricity.ElectricHeater
import altline.appliance.measure.Volume
import altline.appliance.spin.Spinnable
import altline.appliance.substance.Substance
import altline.appliance.substance.transit.SubstanceDrainPort
import altline.appliance.substance.transit.SubstanceSourcePort
import io.nacular.measured.units.*

interface Drum : Spinnable {
    val capacity: Measure<Volume>
    val excessLiquid: Substance
    val excessLiquidAmount: Measure<Volume>
    val load: Set<Body>
    val heater: ElectricHeater

    val inputPort: SubstanceDrainPort
    val outputPort: SubstanceSourcePort

    fun load(vararg items: Body)
    fun unload(vararg items: Body)
    fun unloadAll(): List<Body>
}
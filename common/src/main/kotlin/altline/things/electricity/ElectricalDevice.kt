package altline.things.electricity

import altline.things.electricity.transit.ElectricalDrainPort
import altline.things.measure.Power
import io.nacular.measured.units.*

interface ElectricalDevice {
    val power: Measure<Power>
    val powerInlet: ElectricalDrainPort
}
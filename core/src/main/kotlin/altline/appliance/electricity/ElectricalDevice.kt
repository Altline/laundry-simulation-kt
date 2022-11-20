package altline.appliance.electricity

import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.Power
import io.nacular.measured.units.*

interface ElectricalDevice {
    val power: Measure<Power>
    val powerInlet: ElectricalDrainPort
}
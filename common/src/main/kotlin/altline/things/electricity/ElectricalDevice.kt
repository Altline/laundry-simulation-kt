package altline.things.electricity

import altline.things.electricity.transit.ElectricalDrainPort
import altline.things.measure.Energy
import io.nacular.measured.units.*

interface ElectricalDevice {
    val power: Measure<UnitsRatio<Energy, Time>>
    val powerInlet: ElectricalDrainPort
}
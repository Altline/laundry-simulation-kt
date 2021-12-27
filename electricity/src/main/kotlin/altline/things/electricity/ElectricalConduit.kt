package altline.things.electricity

import altline.things.measure.Energy
import io.nacular.measured.units.Measure

interface ElectricalConduit {
    fun emit(energy: ElectricalEnergy)
    fun collect(amount: Measure<Energy>): ElectricalEnergy
}
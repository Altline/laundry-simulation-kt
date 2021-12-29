package altline.things.electricity

import altline.things.measure.Energy
import altline.things.transit.*
import io.nacular.measured.units.Measure

class ElectricalEnergy(
    override val amount: Measure<Energy>
) : Flowable<Energy>
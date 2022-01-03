package altline.things.electricity

import altline.things.measure.Energy
import altline.things.transit.Flowable
import io.nacular.measured.units.*

class ElectricalEnergy(
    override val amount: Measure<Energy>
) : Flowable<Energy>
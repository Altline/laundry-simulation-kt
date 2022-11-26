package altline.appliance.substance

import altline.appliance.measure.Volume
import io.nacular.measured.units.*

interface Soakable {
    val soakedSubstance: Substance
    val soakRatio: Double
    var freshness: Double

    fun soak(substance: MutableSubstance)
    fun resoakWith(substance: MutableSubstance, amount: Measure<Volume>)
    fun dry(amount: Measure<Volume>): MutableSubstance
}
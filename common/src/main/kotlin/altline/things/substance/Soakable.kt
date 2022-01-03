package altline.things.substance

import altline.things.measure.Volume
import io.nacular.measured.units.*

interface Soakable {
    val soakedSubstance: Substance
    var freshness: Double

    fun soak(substance: Substance)
    fun resoakWith(substance: Substance, amount: Measure<Volume>)
    fun dry(amount: Measure<Volume>): Substance
}
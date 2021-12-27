package altline.things.substances

import altline.things.measure.*
import io.nacular.measured.units.Measure

interface Soakable {
    fun soak(substance: MutableSubstance)
    fun dry(amount: Measure<Volume>): MutableSubstance
}
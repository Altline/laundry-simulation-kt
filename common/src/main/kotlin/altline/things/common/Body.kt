package altline.things.common

import altline.things.measure.Volume
import altline.things.measure.sumOf
import altline.things.substance.MutableSubstance
import altline.things.substance.Substance
import io.nacular.measured.units.Measure

interface Body {
    val volume: Measure<Volume>
    val stainSubstance: Substance

    fun stain(substance: Substance)
    fun clearStain(amount: Measure<Volume>): MutableSubstance
}

val <T : Body> Collection<T>.volume: Measure<Volume>
    get() = sumOf { it.volume }
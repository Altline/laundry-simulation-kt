package altline.appliance.common

import altline.appliance.measure.Volume
import altline.appliance.measure.sumOf
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.Substance
import io.nacular.measured.units.*
import java.util.UUID

interface Body {
    val id: UUID
    val volume: Measure<Volume>
    val stainSubstance: Substance
    val stainRatio: Double

    fun stain(substance: MutableSubstance)
    fun clearStain(amount: Measure<Volume>): MutableSubstance
}

val <T : Body> Collection<T>.volume: Measure<Volume>
    get() = sumOf { it.volume }

val <T : Body> Array<T>.volume: Measure<Volume>
    get() = sumOf { it.volume }
package altline.things.fabric

import altline.things.common.Body
import altline.things.substances.Soakable
import altline.things.measure.*
import altline.things.substances.MutableSubstance
import altline.things.substances.Substance
import io.nacular.measured.units.Measure

abstract class Fabric(
    override val volume: Measure<Volume>
) : Body, Soakable {

    private val stain = MutableSubstance()
    private val soakedSubstance = MutableSubstance()

    val totalVolume: Measure<Volume>
        get() = volume + stain.totalAmount + soakedSubstance.totalAmount

    override fun stain(substance: Substance) {
        stain.add(substance)
    }

    override fun clearStain(amount: Measure<Volume>): MutableSubstance {
        return stain.separate(amount)
    }

    override fun soak(substance: MutableSubstance) {
        val soakableAmount = volume - soakedSubstance.totalAmount
        val soakableSubstance = substance.separate(soakableAmount)
        soakedSubstance.add(soakableSubstance)
    }

    override fun dry(amount: Measure<Volume>): MutableSubstance {
        return soakedSubstance.separate(amount)
    }
}
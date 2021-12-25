package altline.things.fabric

import altline.things.common.Body
import altline.things.substances.Soakable
import altline.things.measure.*
import altline.things.substances.MutableSubstance
import io.nacular.measured.units.Measure

abstract class Fabric(
    override val volume: Measure<Volume>
) : Body, Soakable {

    private val soakedSubstance = MutableSubstance()

    val totalVolume: Measure<Volume>
        get() = volume + soakedSubstance.totalAmount

    override fun soak(substance: MutableSubstance) {
        val soakableAmount = volume - soakedSubstance.totalAmount
        val soakableSubstance = substance.separate(soakableAmount)
        soakedSubstance.add(soakableSubstance)
    }

    override fun dry(amount: Measure<Volume>): MutableSubstance {
        return soakedSubstance.separate(amount)
    }
}
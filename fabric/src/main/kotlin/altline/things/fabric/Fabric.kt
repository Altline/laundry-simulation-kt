package altline.things.fabric

import altline.things.common.Body
import altline.things.measure.Volume
import altline.things.substance.Soakable
import altline.things.substance.Substance
import io.nacular.measured.units.Measure

abstract class Fabric(
    override val volume: Measure<Volume>
) : Body, Soakable {

    override var freshness: Double = 0.0

    private val _stainSubstance = Substance()
    override val stainSubstance = _stainSubstance as Substance

    private val _soakedSubstance = Substance()
    override val soakedSubstance = _soakedSubstance as Substance

    val totalVolume: Measure<Volume>
        get() = volume + stainSubstance.amount + soakedSubstance.amount

    override fun stain(substance: Substance) {
        _stainSubstance.add(substance)
    }

    override fun clearStain(amount: Measure<Volume>): Substance {
        return _stainSubstance.extract(amount)
    }

    override fun soak(substance: Substance) {
        val soakableAmount = volume - soakedSubstance.amount
        val soakableSubstance = substance.extract(soakableAmount)
        _soakedSubstance.add(soakableSubstance)
    }

    override fun resoakWith(substance: Substance, amount: Measure<Volume>) {
        _soakedSubstance.remixWith(substance, amount)
    }

    override fun dry(amount: Measure<Volume>): Substance {
        return _soakedSubstance.extract(amount)
    }
}
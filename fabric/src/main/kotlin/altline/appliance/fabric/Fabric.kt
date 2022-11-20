package altline.appliance.fabric

import altline.appliance.common.Body
import altline.appliance.measure.Volume
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.Soakable
import altline.appliance.substance.Substance
import io.nacular.measured.units.*

abstract class Fabric(
    override val volume: Measure<Volume>
) : Body, Soakable {

    override var freshness: Double = 0.0

    private val _stainSubstance = MutableSubstance()
    override val stainSubstance = _stainSubstance as Substance

    private val _soakedSubstance = MutableSubstance()
    override val soakedSubstance = _soakedSubstance as Substance

    val totalVolume: Measure<Volume>
        get() = volume + stainSubstance.amount + soakedSubstance.amount

    override fun stain(substance: MutableSubstance) {
        _stainSubstance.add(substance)
    }

    override fun clearStain(amount: Measure<Volume>): MutableSubstance {
        return _stainSubstance.extract(amount)
    }

    override fun soak(substance: MutableSubstance) {
        val soakableAmount = volume - soakedSubstance.amount
        val soakableSubstance = substance.extract(soakableAmount)
        _soakedSubstance.add(soakableSubstance)
    }

    override fun resoakWith(substance: MutableSubstance, amount: Measure<Volume>) {
        _soakedSubstance.remixWith(substance, amount)
    }

    override fun dry(amount: Measure<Volume>): MutableSubstance {
        return _soakedSubstance.extract(amount)
    }
}
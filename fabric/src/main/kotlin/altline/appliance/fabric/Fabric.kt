package altline.appliance.fabric

import altline.appliance.measure.Volume
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.SoakableBody
import altline.appliance.substance.Substance
import altline.appliance.substance.TemperatureEqualizer
import io.nacular.measured.units.*
import java.util.*

abstract class Fabric(
    override val volume: Measure<Volume>
) : SoakableBody {

    override val id: UUID = UUID.randomUUID()

    private val _stainSubstance = MutableSubstance()
    override val stainSubstance = _stainSubstance as Substance

    private val _soakedSubstance = MutableSubstance()
    override val soakedSubstance = _soakedSubstance as Substance

    override val temperatureEqualizer = TemperatureEqualizer().apply {
        startFor(_stainSubstance)
        startFor(_soakedSubstance)
    }

    override val stainRatio: Double
        get() = stainSubstance.amount / volume

    override val soakRatio: Double
        get() = soakedSubstance.amount / volume

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
        val extracted = _soakedSubstance.extract(amount)
        val evaporating = extracted.extractAllEvaporating()
        stain(extracted)
        return evaporating
    }
}
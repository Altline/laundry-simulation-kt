package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.common.volume
import altline.things.electricity.ElectricHeater
import altline.things.measure.Spin
import altline.things.measure.Temperature.Companion.celsius
import altline.things.measure.Volume
import altline.things.measure.Volume.Companion.liters
import altline.things.substance.MutableSubstance
import altline.things.substance.Soakable
import altline.things.substance.fresheningPotential
import altline.things.substance.transit.Reservoir
import altline.things.washing.cleaningPower
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class BasicDrum(
    capacity: Measure<Volume>,
    override val heater: ElectricHeater,
    private val config: LaundryWasherConfig
) : Drum,
    Reservoir(
        capacity,
        0.0 * liters,
        false,
        config.intakeFlowRate,
        config.outputFlowRate
    ) {

    override val inputPort = inputs[0]
    override val outputPort = outputs[0]

    private val _laundry = mutableListOf<Body>()
    val laundry = _laundry as List<Body>

    override val excessLiquidAmount: Measure<Volume>
        get() = storedSubstanceAmount

    init {
        heater.heatedSubstance = storedSubstance
    }

    override fun load(item: Body) {
        if (doesFit(item)) {
            _laundry += item
        }
    }

    override fun load(items: Collection<Body>) {
        if (doesFit(items)) {
            _laundry += items
        }
    }

    private fun doesFit(item: Body) = laundry.volume + item.volume <= capacity
    private fun doesFit(items: Collection<Body>) = laundry.volume + items.volume <= capacity

    override fun unload(item: Body) {
        _laundry -= item
    }

    override fun unload(items: Collection<Body>) {
        _laundry -= items.toSet()
    }

    override fun unload(): List<Body> {
        return ArrayList(laundry).also { _laundry.clear() }
    }

    override fun pushFlow(flowable: MutableSubstance, timeFrame: Measure<Time>, flowId: Long): Measure<Volume> {
        return super.pushFlow(flowable, timeFrame, flowId).also {
            soakLaundry()
        }
    }

    private fun soakLaundry() {
        laundry.forEach { piece ->
            if (piece is Soakable) {
                piece.soak(storedSubstance)
            }
        }
    }

    override fun spin(speed: Measure<Spin>, duration: Measure<Time>) {
        for (piece in laundry) {
            wash(piece, speed, duration `in` seconds)
        }
    }

    private fun wash(body: Body, spinSpeed: Measure<Spin>, seconds: Double) {
        if (spinSpeed > config.centrifugeThreshold) return

        val spinEffectiveness = spinSpeed / config.centrifugeThreshold

        if (body is Soakable) {
            // resoak
            val resoakAmount = minOf(body.soakedSubstance.amount, excessLiquidAmount) *
                    config.nominalResoakFactor * spinEffectiveness
            body.resoakWith(storedSubstance, resoakAmount)

            // freshen
            val diff = body.soakedSubstance.fresheningPotential - body.freshness
            val step = diff / 10 * spinEffectiveness * seconds
            body.freshness += step
        }

        // clean
        val effectiveCleaningPower = calcCleaningPower(body) * spinEffectiveness
        val stainAmountToClear = body.stainSubstance.amount * effectiveCleaningPower * seconds
        val clearedStain = body.clearStain(stainAmountToClear)
        storedSubstance.add(clearedStain)
    }

    private fun calcCleaningPower(body: Body): Double {
        return if (body is Soakable) {
            val soakRatio = body.soakedSubstance.amount / body.volume
            val soakCoefficient = ((soakRatio - config.lowerSoakRatio) / config.upperSoakRatio - config.lowerSoakRatio)
                .coerceIn(0.0, 1.0)
            val temperatureCoefficient = (body.soakedSubstance.temperature / (100 * celsius))
                .coerceIn(0.0, 1.0)
            body.soakedSubstance.cleaningPower * soakCoefficient * temperatureCoefficient

        } else {
            val temperatureCoefficient = (storedSubstance.temperature / (100 * celsius))
                .coerceIn(0.0, 1.0)
            storedSubstance.cleaningPower * temperatureCoefficient
        }
    }
}
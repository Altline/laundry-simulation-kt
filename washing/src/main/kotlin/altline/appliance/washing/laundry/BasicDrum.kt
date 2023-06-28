package altline.appliance.washing.laundry

import altline.appliance.common.Body
import altline.appliance.common.volume
import altline.appliance.electricity.ElectricHeater
import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.spin.SpinDirection
import altline.appliance.substance.*
import altline.appliance.substance.transit.Reservoir
import altline.appliance.washing.cleaningPower
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

    private val _load = mutableSetOf<Body>()
    override val load = _load as Set<Body>

    override val excessLiquid: Substance
        get() = storedSubstance

    override val excessLiquidAmount: Measure<Volume>
        get() = storedSubstanceAmount

    init {
        heater.heatedSubstance = storedSubstance
        TemperatureEqualizer().startFor(storedSubstance)
    }

    override fun load(vararg items: Body) {
        if (load.volume + items.volume <= capacity) {
            _load += items
            items.forEach { item ->
                item.temperatureEqualizer.overridingAmbientSubstance = storedSubstance
            }
        }
    }

    override fun unload(vararg items: Body) {
        _load -= items.toSet()
        items.forEach {
            it.temperatureEqualizer.overridingAmbientSubstance = null
        }
    }

    override fun unloadAll(): List<Body> {
        return ArrayList(load).also { _load.clear() }
    }

    override fun pushFlow(flowable: MutableSubstance, timeFrame: Measure<Time>, flowId: Long): Measure<Volume> {
        return super.pushFlow(flowable, timeFrame, flowId).also {
            soakLaundry()
        }
    }

    private fun soakLaundry() {
        load.forEach { piece ->
            if (piece is Soakable) {
                val soakAmount = 0.01 * (piece.volume - piece.soakedSubstance.amount)
                piece.soak(storedSubstance.extract(soakAmount))
            }
        }
    }

    override fun spin(direction: SpinDirection, speed: Measure<Spin>, duration: Measure<Time>) {
        for (piece in load) {
            if (speed < config.centrifugeThreshold) wash(piece, speed, duration `in` seconds)
            else dry(piece, speed, duration `in` seconds)
        }
    }

    private fun wash(body: Body, spinSpeed: Measure<Spin>, seconds: Double) {
        val spinEffectiveness = spinSpeed / config.centrifugeThreshold

        if (body is Soakable) {
            if (body.soakRatio < 1) {
                body.soak(storedSubstance)
            }

            val maxResoakAmount = minOf(body.soakedSubstance.amount, excessLiquidAmount)
            val resoakFactor = config.nominalResoakFactor * spinEffectiveness
            val resoakAmount = calcGrowth(maxResoakAmount, resoakFactor, seconds)
            body.resoakWith(storedSubstance, resoakAmount)
        }

        val effectiveCleaningPower = calcCleaningPower(body) * spinEffectiveness
        val stainAmountToClear = calcGrowth(body.stainSubstance.amount, effectiveCleaningPower, seconds)
        val clearedStain = body.clearStain(stainAmountToClear)
        storedSubstance.add(clearedStain)
    }

    private fun calcCleaningPower(body: Body): Double {
        return if (body is Soakable) {
            if (body.soakedSubstance.isEmpty()) return 0.0

            val soakCoefficient =
                ((body.soakRatio - config.lowerSoakRatio) / (config.upperSoakRatio - config.lowerSoakRatio))
                    .coerceIn(0.0, 1.0)
            val temperatureCoefficient = (body.soakedSubstance.temperature!! / (100 * celsius))
                .coerceIn(0.0, 1.0)
            body.soakedSubstance.cleaningPower * soakCoefficient * temperatureCoefficient

        } else {
            if (storedSubstance.isEmpty()) return 0.0

            val temperatureCoefficient = (storedSubstance.temperature!! / (100 * celsius))
                .coerceIn(0.0, 1.0)
            storedSubstance.cleaningPower * temperatureCoefficient
        }
    }

    private fun dry(body: Body, spinSpeed: Measure<Spin>, seconds: Double) {
        if (body !is Soakable) return

        val dryingRate = (spinSpeed `in` rpm) / 600000
        val amountToDry = calcGrowth(body.soakedSubstance.amount, dryingRate, seconds)
        val driedSubstance = body.dry(amountToDry)
        storedSubstance.add(driedSubstance)
    }
}
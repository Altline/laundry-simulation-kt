package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.common.volume
import altline.things.measure.Spin
import altline.things.measure.Volume
import altline.things.measure.Volume.Companion.liters
import altline.things.measure.delay
import altline.things.measure.sumOf
import altline.things.substance.MutableSubstance
import altline.things.substance.Soakable
import altline.things.substance.fresheningPotential
import altline.things.substance.transit.Reservoir
import altline.things.washing.cleaningPower
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.delay

class BasicDrum(
    capacity: Measure<Volume>,
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

    private val totalLiquid: Measure<Volume>
        get() {
            val soakables = laundry.filterIsInstance<Soakable>()
            val soakedAmount = soakables.sumOf { it.soakedSubstance.amount }
            return soakedAmount + excessLiquidAmount
        }

    private val soakRatio: Double
        get() = totalLiquid / laundry.volume

    private val effectiveCleaningPower: Double
        get() {
            val soakCoefficient = (soakRatio - config.lowerSoakRatio) / config.upperSoakRatio - config.lowerSoakRatio
            return storedSubstance.cleaningPower * soakCoefficient.coerceIn(0.0, 1.0)
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

    override suspend fun spin(speed: Measure<Spin>, duration: Measure<Time>) {
        if (effectiveCleaningPower == 0.0 || laundry.isEmpty()) {
            delay(duration)
            return
        }

        val seconds = (duration `in` seconds).toInt()
        repeat(seconds) {
            delay(1000)
            for (piece in laundry) {
                wash(piece, speed)
            }
        }
    }

    private fun wash(body: Body, spinSpeed: Measure<Spin>) {
        if (spinSpeed > config.centrifugeThreshold) return

        val spinEffectiveness = spinSpeed / config.centrifugeThreshold
        val finalCleaningPower = effectiveCleaningPower * spinEffectiveness

        val stainAmountToClear = body.stainSubstance.amount * finalCleaningPower
        val clearedStain = body.clearStain(stainAmountToClear)
        storedSubstance.add(clearedStain)

        if (body is Soakable) {
            val resoakAmount = minOf(body.soakedSubstance.amount, excessLiquidAmount) *
                    config.nominalResoakFactor * spinEffectiveness
            body.resoakWith(storedSubstance, resoakAmount)

            val diff = body.soakedSubstance.fresheningPotential - body.freshness
            val step = diff / 10 * spinEffectiveness
            body.freshness += step
        }
    }
}
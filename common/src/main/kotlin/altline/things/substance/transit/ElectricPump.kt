package altline.things.substance.transit

import altline.things.electricity.ElectricalDevice
import altline.things.electricity.MutableElectricalEnergy
import altline.things.electricity.transit.ElectricalDrain
import altline.things.electricity.transit.ElectricalDrainPort
import altline.things.electricity.transit.ElectricalSource
import altline.things.measure.Energy
import altline.things.measure.Power
import altline.things.measure.Volume
import altline.things.measure.divSameUnit
import altline.things.transit.DefaultFlowTimeFrame
import altline.things.util.CoroutineManager
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random

class ElectricPump(
    override val power: Measure<Power>,
    maxFlowRate: Measure<UnitsRatio<Volume, Time>>,
    inputCount: Int = 1,
    outputCount: Int = 1
) : Pump(maxFlowRate, inputCount, outputCount), ElectricalDevice {

    private val electricalDrain = object : ElectricalDrain {
        override val maxInputFlowRate = power
        override val realInputFlowRate = power
        override val inputs = arrayOf(ElectricalDrainPort(this))
        override val inputCount = inputs.size

        override fun pushFlow(
            flowable: MutableElectricalEnergy,
            timeFrame: Measure<Time>,
            flowId: Long
        ): Measure<Energy> {
            return flowable.amount
        }
    }

    override val powerInlet = electricalDrain.inputs[0]
    private val powerSource: ElectricalSource? = powerInlet.connectedPort?.owner

    var powerSetting: Double
        get() = realFlowRate.divSameUnit(maxFlowRate)
        set(value) {
            require(value in 0.0..1.0)
            realFlowRate = value * maxFlowRate
        }

    val running: Boolean
        get() = coroutineManager.active

    private val coroutineManager = CoroutineManager(CoroutineScope(Dispatchers.Default)) {
        tryPump()
    }

    private fun tryPump() {
        val timeFrame = DefaultFlowTimeFrame
        val requiredEnergy = (power * timeFrame) * powerSetting
        val availableEnergy = powerSource?.pullFlow(requiredEnergy, timeFrame, Random.nextLong())?.amount
        if (availableEnergy != null) {
            val energyRatio = availableEnergy / requiredEnergy
            val pumpAmount = (realFlowRate * timeFrame) * energyRatio
            pump(pumpAmount, timeFrame)
        }
    }

    fun start() {
        coroutineManager.active = true
    }

    fun stop() {
        coroutineManager.active = false
    }
}

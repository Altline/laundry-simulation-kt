package altline.things.substance.transit

import altline.things.electricity.BasicElectricalDevice
import altline.things.measure.Power
import altline.things.measure.Volume
import altline.things.measure.divSameUnit
import altline.things.transit.DefaultFlowTimeFrame
import io.nacular.measured.units.*
import kotlin.random.Random

class ElectricPump(
    power: Measure<Power>,
    maxFlowRate: Measure<UnitsRatio<Volume, Time>>,
    inputCount: Int = 1,
    outputCount: Int = 1
) : BasicElectricalDevice(power) {

    private val pipe = object : BasicSubstanceConduit(maxFlowRate, inputCount, outputCount) {
        override var realInputFlowRate: Measure<UnitsRatio<Volume, Time>>
            get() = super.realInputFlowRate
            public set(value) { super.realInputFlowRate = value }

        override var realOutputFlowRate: Measure<UnitsRatio<Volume, Time>>
            get() = super.realOutputFlowRate
            public set(value) { super.realOutputFlowRate = value }
    }

    val substanceInputs = pipe.inputs
    val substanceOutputs = pipe.outputs

    val maxFlowRate: Measure<UnitsRatio<Volume, Time>>
        get() = minOf(pipe.maxInputFlowRate, pipe.maxOutputFlowRate)

    var realFlowRate: Measure<UnitsRatio<Volume, Time>>
        get() = minOf(pipe.realInputFlowRate, pipe.realOutputFlowRate)
        private set(value) {
            pipe.realInputFlowRate = value
            pipe.realOutputFlowRate = value
        }

    var powerSetting: Double
        get() = realFlowRate.divSameUnit(maxFlowRate)
        set(value) {
            require(value in 0.0..1.0)
            realFlowRate = value * maxFlowRate
        }

    private fun pump(amount: Measure<Volume>, timeFrame: Measure<Time>) {
        val chunk = pipe.pullFlow(amount, timeFrame, Random.nextLong())
        if (chunk != null) {
            pipe.pushFlow(chunk, timeFrame, Random.nextLong())
        }
    }

    override fun operate() {
        val timeFrame = DefaultFlowTimeFrame
        val requiredEnergy = (power * timeFrame) * powerSetting
        val availableEnergy = powerSource?.pullFlow(requiredEnergy, timeFrame, Random.nextLong())?.amount
        if (availableEnergy != null) {
            val energyRatio = availableEnergy / requiredEnergy
            val pumpAmount = (realFlowRate * timeFrame) * energyRatio
            pump(pumpAmount, timeFrame)
        }
    }
}

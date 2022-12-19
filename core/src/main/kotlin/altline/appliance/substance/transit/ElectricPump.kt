package altline.appliance.substance.transit

import altline.appliance.electricity.BasicElectricalDevice
import altline.appliance.measure.Power
import altline.appliance.measure.Volume
import altline.appliance.measure.VolumetricFlow
import io.nacular.measured.units.*
import kotlin.random.Random

class ElectricPump(
    power: Measure<Power>,
    maxFlowRate: Measure<VolumetricFlow>,
    inputCount: Int = 1,
    outputCount: Int = 1
) : BasicElectricalDevice(power) {

    private val pipe = Valve(maxFlowRate, inputCount, outputCount)

    val substanceInputs = pipe.inputs
    val substanceOutputs = pipe.outputs

    var powerSetting: Double = 1.0
        set(value) {
            require(value in 0.0..1.0)
            field = value
        }

    private fun pump(amount: Measure<Volume>, timeFrame: Measure<Time>) {
        val chunk = pipe.pullFlow(amount, timeFrame, Random.nextLong())
        if (chunk != null) {
            pipe.pushFlow(chunk, timeFrame, Random.nextLong())
        }
    }

    override fun onStart() {
        pipe.open()
    }

    override fun onStop() {
        pipe.close()
    }

    override fun operate() {
        val requiredEnergy = (power * tickPeriod) * powerSetting
        val availableEnergy = pullEnergy(requiredEnergy, tickPeriod)?.amount
        if (availableEnergy != null) {
            val energyRatio = availableEnergy / requiredEnergy
            val pumpAmount = pipe.maxFlowRate * powerSetting * energyRatio * tickPeriod
            pump(pumpAmount, tickPeriod)
        }
    }
}

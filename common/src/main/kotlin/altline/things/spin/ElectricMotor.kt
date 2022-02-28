package altline.things.spin

import altline.things.electricity.BasicElectricalDevice
import altline.things.measure.Power
import altline.things.measure.Spin
import altline.things.measure.Spin.Companion.rpm
import io.nacular.measured.units.*

class ElectricMotor(
    power: Measure<Power>,
    val maxSpeed: Measure<Spin>
) : BasicElectricalDevice(power) {

    var connectedLoad: Spinnable? = null

    var speedSetting: Measure<Spin> = maxSpeed
        set(value) {
            require(value in (0.0 * rpm)..maxSpeed)
            field = value
        }

    override fun operate() {
        val requiredEnergy = (power * tickPeriod) * (speedSetting / maxSpeed)
        val availableEnergy = pullEnergy(requiredEnergy, tickPeriod)?.amount
        if (availableEnergy != null) {
            val energyRatio = availableEnergy / requiredEnergy
            val speed = speedSetting * energyRatio
            connectedLoad?.spin(speed, tickPeriod)
        }
    }
}
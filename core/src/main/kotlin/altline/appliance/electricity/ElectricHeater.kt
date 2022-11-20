package altline.appliance.electricity

import altline.appliance.measure.Energy.Companion.calories
import altline.appliance.measure.Power
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.substance.MutableSubstance
import io.nacular.measured.units.*

class ElectricHeater(
    power: Measure<Power>
) : BasicElectricalDevice(power) {

    var heatedSubstance: MutableSubstance? = null

    var powerSetting: Double = 1.0
        set(value) {
            require(value in 0.0..1.0)
            field = value
        }

    override fun operate() {
        val requiredEnergy = (power * tickPeriod) * powerSetting
        val availableEnergy = pullEnergy(requiredEnergy, tickPeriod)?.amount
        if (availableEnergy != null) {
            heatedSubstance?.let {
                val calories = availableEnergy `in` calories
                val milliliters = it.amount `in` milliliters
                // Here the assumption is that every substance requires the same amount of energy to be equally heated
                // = 1 calorie to heat 1 milliliter of substance 1 degree celsius
                it.temperature += (calories / milliliters) * celsius
            }
        }
    }
}
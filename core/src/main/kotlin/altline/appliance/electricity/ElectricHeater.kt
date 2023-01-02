package altline.appliance.electricity

import altline.appliance.measure.Energy.Companion.calories
import altline.appliance.measure.Power
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.isNotEmpty
import io.nacular.measured.units.Measure
import io.nacular.measured.units.times

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
        val requiredEnergy = (power * timeFactor) * powerSetting
        val availableEnergy = pullEnergy(requiredEnergy, timeFactor)?.amount
        if (availableEnergy != null) {
            heatedSubstance?.let { heatedSubstance ->
                synchronized(heatedSubstance) {
                    if (heatedSubstance.isNotEmpty()) {
                        val calories = availableEnergy `in` calories
                        val milliliters = heatedSubstance.amount `in` milliliters
                        val temperature = heatedSubstance.temperature!!
                        // Here the assumption is that every substance requires the same amount of energy to be equally heated
                        // = 1 calorie to heat 1 milliliter of substance 1 degree Celsius
                        heatedSubstance.temperature = temperature + (calories / milliliters) * celsius
                    }
                }
            }
        }
    }
}
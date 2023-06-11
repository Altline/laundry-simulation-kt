package altline.appliance.substance

import altline.appliance.common.DefaultAmbientTemperature
import altline.appliance.common.RefreshPeriod
import altline.appliance.common.TimeFactor
import altline.appliance.measure.Temperature
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.repeatPeriodically
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A manager for substances that need to passively equalize their temperature to the given [ambientTemperature].
 */
class TemperatureEqualizer(
    var ambientTemperature: Measure<Temperature> = DefaultAmbientTemperature,
    var overridingAmbientSubstance: Substance? = null
) {
    /**
     * Launches a coroutine that periodically changes the temperature of the [substance] so that it approaches the [ambientTemperature].
     *
     * Note: At the moment of making, the cancellation of coroutines launched here was not necessary.
     * If the substances being used here ever need to be garbage collected, the cancellation needs to be handled.
     */
    fun startFor(substance: MutableSubstance) {
        CoroutineScope(Dispatchers.Default).launch {
            repeatPeriodically(RefreshPeriod) {
                synchronized(substance) {
                    with(substance) {
                        // Heat capacity of substances and thermal isolation of the environment are not modelled
                        temperature?.let {
                            val tempDiff = it - (overridingAmbientSubstance?.temperature ?: ambientTemperature)
                            val litersAmount = amount `in` liters
                            val changeAmount = (tempDiff / litersAmount * 0.001 * (TimeFactor `in` Time.seconds))
                                .coerceAtMost(tempDiff)
                            temperature = temperature!! - changeAmount
                        }
                    }
                }
            }
        }
    }
}
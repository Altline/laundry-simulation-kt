package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import io.nacular.measured.units.*

class SpinCycle : WashCycleBase() {
    override val temperatureSettings: List<Measure<Temperature>> = emptyList()
    override val spinSpeedSettings: List<Measure<Spin>> = listOf(600, 800, 1000, 1200, 1400, 1600)
        .map { it * Spin.rpm }.toList()

    init {
        addStage {
            spinPhase {
                section(
                    duration = 0.1 * Time.minutes,
                    spinSpeed = 500 * Spin.rpm
                )
                section(
                    duration = 1 * Time.minutes,
                    endDelay = 32 * Time.seconds
                )
            }
        }

        selectedTemperatureSettingIndex = null
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }
}
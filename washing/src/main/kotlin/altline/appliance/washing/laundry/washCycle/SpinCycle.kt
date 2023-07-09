package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds

class SpinCycle : WashCycleBase() {
    override val temperatureSettings: List<Measure<Temperature>> = emptyList()
    override val spinSpeedSettings: List<Measure<Spin>> = listOf(600, 800, 1000, 1200, 1400, 1600)
        .map { it * rpm }.toList()

    init {
        selectedTemperatureSettingIndex = null
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }

    override fun getStages(): List<CycleStage> {
        return buildCycle {
            stage {
                spinPhase {
                    section(
                        duration = 0.1 * minutes,
                        spinSpeed = 500 * rpm
                    )
                    section(
                        duration = 1 * minutes,
                        endDelay = 32 * seconds
                    )
                }
            }
        }
    }
}
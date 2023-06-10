package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume.Companion.liters
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds

class CottonCycle : WashCycleBase() {
    override val temperatureSettings: List<Measure<Temperature>> = listOf(20, 30, 40, 60, 90)
        .map { it * celsius }
    override val spinSpeedSettings: List<Measure<Spin>> = listOf(0, 600, 800, 1000, 1200, 1400, 1600)
        .map { it * rpm }

    init {
        addStage {
            detergentFillPhase(1 * liters)
            washPhase {
                section(
                    duration = 0.2 * minutes,
                    spinPeriod = 5 * seconds,
                    restPeriod = 5 * seconds,
                    spinSpeed = 50 * rpm
                )
                section(
                    duration = 0.2 * minutes,
                    spinPeriod = 5 * seconds,
                    restPeriod = 5 * seconds,
                    spinSpeed = 50 * rpm
                )
            }
            drainPhase(
                duration = 0.1 * minutes,
                spinPeriod = 5 * seconds,
                restPeriod = 5 * seconds,
                spinSpeed = 60 * rpm
            )
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

        selectedTemperatureSettingIndex = 0
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }
}
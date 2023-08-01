package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature
import altline.appliance.measure.Volume.Companion.liters
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds

class RinseCycle : WashCycleBase() {
    override val temperatureSettings: List<Measure<Temperature>> = emptyList()
    override val spinSpeedSettings: List<Measure<Spin>> =
        listOf(0, 600, 800, 1000, 1200, 1400, 1600).map { it * rpm }

    init {
        selectedTemperatureSettingIndex = null
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }

    override val selectedSpinSpeedSetting: Measure<Spin>
        get() = super.selectedSpinSpeedSetting ?: (0 * rpm)

    override fun getStages(): List<CycleStage> {
        return buildCycle {
            stage {
                softenerFillPhase(15 * liters)
                washPhase {
                    section(
                        duration = 8 * minutes,
                        spinPeriod = 15 * seconds,
                        restPeriod = 7 * seconds,
                        spinSpeed = 60 * rpm
                    )
                }
                drainPhase(
                    duration = 2 * minutes,
                    spinPeriod = 10 * seconds,
                    restPeriod = 5 * seconds,
                    spinSpeed = 60 * rpm
                )
                spinPhase {
                    section(
                        duration = 1 * minutes,
                        spinSpeed = 400 * rpm
                    )
                    section(
                        duration = 8 * minutes,
                        spinSpeed = selectedSpinSpeedSetting,
                        adjustableSpeed = true,
                        endDelay = 32 * seconds
                    )
                }
            }
        }
    }
}
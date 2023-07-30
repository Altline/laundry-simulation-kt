package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume.Companion.liters
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds

class CottonCycle : WashCycleBase(), PreWashCapable {
    override val temperatureSettings: List<Measure<Temperature>> =
        listOf(20, 30, 40, 60, 90).map { it * celsius }
    override val spinSpeedSettings: List<Measure<Spin>> =
        listOf(0, 600, 800, 1000, 1200, 1400, 1600).map { it * rpm }

    override var preWash: Boolean = false

    override val selectedTemperatureSetting: Measure<Temperature>
        get() = super.selectedTemperatureSetting ?: (0 * celsius)

    override val selectedSpinSpeedSetting: Measure<Spin>
        get() = super.selectedSpinSpeedSetting ?: (0 * rpm)

    init {
        selectedTemperatureSettingIndex = 0
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }

    override fun getStages(): List<CycleStage> {
        return buildCycle {
            if (preWash) preWashStage()
            mainWashStage()
            rinseStage()
            rinseStage()
            softenerStage()
        }
    }

    private fun StageBuilder.preWashStage() {
        stage {
            preWashFillPhase(10  * liters)
            washPhase {
                section(
                    duration = 15 * minutes,
                    spinPeriod = 30 * seconds,
                    restPeriod = 15 * seconds,
                    spinSpeed = 50 * rpm
                )
            }
            drainPhase(
                duration = 1 * minutes,
                spinPeriod = 8 * seconds,
                restPeriod = 4 * seconds,
                spinSpeed = 60 * rpm
            )
        }
    }

    private fun StageBuilder.mainWashStage() {
        stage {
            detergentFillPhase(14 * liters)
            washPhase {
                section(
                    duration = 15 * minutes,
                    spinPeriod = 60 * seconds,
                    restPeriod = 30 * seconds,
                    spinSpeed = 40 * rpm,
                    temperature = selectedTemperatureSetting
                )
                section(
                    duration = 60 * minutes,
                    spinPeriod = 15 * seconds,
                    restPeriod = 7 * seconds,
                    spinSpeed = 60 * rpm,
                    temperature = selectedTemperatureSetting
                )
                section(
                    duration = 10 * minutes,
                    spinPeriod = 60 * seconds,
                    restPeriod = 30 * seconds,
                    spinSpeed = 78 * rpm,
                    temperature = selectedTemperatureSetting
                )
                section(
                    duration = 30 * minutes,
                    spinPeriod = 15 * seconds,
                    restPeriod = 7 * seconds,
                    spinSpeed = 60 * rpm,
                    temperature = selectedTemperatureSetting
                )
            }
            drainPhase(
                duration = 1 * minutes,
                spinPeriod = 10 * seconds,
                restPeriod = 5 * seconds,
                spinSpeed = 60 * rpm
            )
            spinPhase(
                duration = 2 * minutes,
                spinSpeed = 600 *rpm,
                endDelay = 32 * seconds
            )
        }
    }

    private fun StageBuilder.rinseStage() {
        stage {
            detergentFillPhase(15 * liters)
            washPhase {
                section(
                    duration = 7 * minutes,
                    spinPeriod = 15 * seconds,
                    restPeriod = 7 * seconds,
                    spinSpeed = 60 * rpm
                )
            }
            drainPhase(
                duration = 1 * minutes,
                spinPeriod = 10 * seconds,
                restPeriod = 5 * seconds,
                spinSpeed = 60 * rpm
            )
            spinPhase(
                duration = 2 * minutes,
                spinSpeed = 600 *rpm,
                endDelay = 32 * seconds
            )
        }
    }

    private fun StageBuilder.softenerStage() {
        stage {
            softenerFillPhase(16 * liters)
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
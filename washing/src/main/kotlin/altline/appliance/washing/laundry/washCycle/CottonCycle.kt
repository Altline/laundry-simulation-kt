package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume
import io.nacular.measured.units.*

// TODO
class CottonCycle : WashCycleBase() {
    override val temperatureSettings: List<Measure<Temperature>> = listOf(20, 30, 40, 60, 90)
        .map { it * celsius }
    override val spinSpeedSettings: List<Measure<Spin>> = (600..1400 step 200)
        .map { it * rpm }.toList()

    private lateinit var mainWashParams: WashParams
    private lateinit var spinPhaseParams: CentrifugeParams

    init {
        addStage {
            detergentFillPhase(15 * Volume.liters)
            washPhase {
                section(
                    duration = 10 * Time.minutes,
                    spinPeriod = 5 * Time.seconds,
                    restPeriod = 5 * Time.seconds,
                    spinSpeed = 50 * rpm
                ).also {
                    this@CottonCycle.mainWashParams = it.washParams
                }
            }
            drainPhase(
                duration = 1.5 * Time.minutes,
                spinPeriod = 5 * Time.seconds,
                restPeriod = 5 * Time.seconds,
                spinSpeed = 60 * rpm
            )
            spinPhase(
                duration = 5 * Time.minutes,
                spinSpeed = 0 * rpm
            ).also {
                this@CottonCycle.spinPhaseParams = it.params
            }
        }

        selectedTemperatureSettingIndex = 0
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }

    override fun onTemperatureChanged(value: Measure<Temperature>?) {
        mainWashParams.temperature = value
    }

    override fun onSpinSpeedChanged(value: Measure<Spin>?) {
        spinPhaseParams.spinSpeed = value ?: (0 * rpm)
    }
}
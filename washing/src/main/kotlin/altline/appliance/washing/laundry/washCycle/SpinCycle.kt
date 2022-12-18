package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import io.nacular.measured.units.*

class SpinCycle : WashCycleBase() {
    override val temperatureSettings: List<Measure<Temperature>> = listOf()
    override val spinSpeedSettings: List<Measure<Spin>> = (600..1400 step 200)
        .map { it * Spin.rpm }.toList()

    private lateinit var spinPhaseParams: CentrifugeParams

    init {
        addStage {
            spinPhase(
                duration = 5 * Time.minutes,
                spinSpeed = 0 * Spin.rpm
            ).also {
                this@SpinCycle.spinPhaseParams = it.params
            }
        }

        selectedTemperatureSettingIndex = null
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }

    override fun onTemperatureChanged(value: Measure<Temperature>?) {
        /* no-op (temperature not expected to change) */
    }

    override fun onSpinSpeedChanged(value: Measure<Spin>?) {
        spinPhaseParams.spinSpeed = value ?: (0 * Spin.rpm)
    }
}
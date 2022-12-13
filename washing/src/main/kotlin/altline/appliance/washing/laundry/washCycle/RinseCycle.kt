package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature
import altline.appliance.measure.Volume.Companion.liters
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds

class RinseCycle : WashCycleBase() {
    override val temperatureSettings: List<Measure<Temperature>> = listOf()
    override val spinSpeedSettings: List<Measure<Spin>> = (600..1400 step 200).map { it * rpm }.toList()

    private lateinit var spinPhaseParams: CentrifugeParams

    init {
        addStage {
            detergentFillPhase(15 * liters)
            washPhase {
                section(
                    duration = 10 * minutes,
                    spinPeriod = 5 * seconds,
                    restPeriod = 5 * seconds,
                    spinSpeed = 60 * rpm
                )
            }
            drainPhase(
                duration = 1.5 * minutes,
                spinPeriod = 5 * seconds,
                restPeriod = 5 * seconds,
                spinSpeed = 60 * rpm
            )
            spinPhase(
                duration = 5 * minutes,
                spinSpeed = this@RinseCycle.selectedSpinSpeedSetting ?: (0 * rpm)
            ).also {
                this@RinseCycle.spinPhaseParams = it.params
            }
        }

        selectedTemperatureSettingIndex = null
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }

    override fun onTemperatureChanged(value: Measure<Temperature>?) {
        /* no-op (temperature not expected to change) */
    }

    override fun onSpinSpeedChanged(value: Measure<Spin>?) {
        spinPhaseParams.spinSpeed = value ?: (0 * rpm)
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}
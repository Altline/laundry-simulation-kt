package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.washing.laundry.washCycle.phase.DetergentFillPhase
import altline.appliance.washing.laundry.washCycle.phase.DrainPhase
import altline.appliance.washing.laundry.washCycle.phase.SpinPhase
import altline.appliance.washing.laundry.washCycle.phase.WashPhase
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds

class RinseCycle : WashCycleBase(
    stages = listOf(
        CycleStage(
            phases = listOf(
                DetergentFillPhase(
                    fillToAmount = 15 * liters
                ),
                WashPhase(
                    WashPhase.Section(
                        WashParams(
                            duration = 10 * minutes,
                            spinPeriod = 5 * seconds,
                            restPeriod = 5 * seconds,
                            spinSpeed = 60 * rpm
                        )
                    )
                ),
                DrainPhase(
                    spinParams = WashParams(
                        duration = 1.5 * minutes,
                        spinPeriod = 5 * seconds,
                        restPeriod = 5 * seconds,
                        spinSpeed = 60 * rpm
                    )
                ),
                SpinPhase(
                    CentrifugeParams(
                        duration = 5 * minutes,
                        spinSpeed = 1000 * rpm
                    )
                )
            )
        )
    )
) {
    override val temperatureSettings: List<Measure<Temperature>> = listOf()
    override val spinSpeedSettings: List<Measure<Spin>> = (600..1400 step 200).map { it * rpm }.toList()

    init {
        selectedTemperatureSettingIndex = null
        selectedSpinSpeedSettingIndex = spinSpeedSettings.lastIndex
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}
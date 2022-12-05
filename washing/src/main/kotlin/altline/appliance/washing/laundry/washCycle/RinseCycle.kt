package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin.Companion.rpm
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
                    fillToAmount = 30 * liters
                ),
                WashPhase(
                    sections = listOf(
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
    override fun stop() {
        TODO("Not yet implemented")
    }
}
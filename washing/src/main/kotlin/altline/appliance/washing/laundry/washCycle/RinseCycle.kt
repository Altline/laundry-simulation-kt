package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.washing.laundry.CentrifugeParams
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.WashParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds

class RinseCycle : WashCycleBase() {

    override val name = "Rinse"

    private val rinseWaterLevel = 30.0 * liters

    private val rinseParams = WashParams(
        washDuration = 10 * minutes,
        spinDuration = 5 * seconds,
        restDuration = 5 * seconds,
        spinSpeed = 60 * rpm
    )

    private val centrifugeParams = CentrifugeParams(
        duration = 5 * minutes,
        spinSpeed = 1000 * rpm
    )

    override suspend fun executeProgram(washer: StandardLaundryWasherBase) {
        with(washer) {
            fillThroughDetergent(rinseWaterLevel)
            wash(rinseParams)
            drain()
            centrifuge(centrifugeParams)
        }
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}
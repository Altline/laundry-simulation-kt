package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import io.nacular.measured.units.*

data class TumbleParams(
    val duration: Measure<Time>,
    val spinPeriod: Measure<Time>,
    val restPeriod: Measure<Time>,
    val spinSpeed: Measure<Spin>,
    val temperature: Measure<Temperature>? = null
)
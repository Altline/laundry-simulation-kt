package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import io.nacular.measured.units.*

data class WashParams(
    val duration: Measure<Time>,
    val spinPeriod: Measure<Time>,
    val restPeriod: Measure<Time>,
    val spinSpeed: Measure<Spin>
)
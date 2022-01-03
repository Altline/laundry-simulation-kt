package altline.things.washing.laundry

import altline.things.measure.Spin
import io.nacular.measured.units.*

data class WashParams(
    val washDuration: Measure<Time>,
    val spinDuration: Measure<Time>,
    val restDuration: Measure<Time>,
    val spinSpeed: Measure<Spin>
)
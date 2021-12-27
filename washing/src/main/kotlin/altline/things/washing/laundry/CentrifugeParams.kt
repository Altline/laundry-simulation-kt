package altline.things.washing.laundry

import altline.things.measure.Spin
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time

data class CentrifugeParams(
    val duration: Measure<Time>,
    val spinSpeed: Measure<Spin>
)
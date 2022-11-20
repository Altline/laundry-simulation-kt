package altline.appliance.spin

import altline.appliance.measure.Spin
import io.nacular.measured.units.*

interface Spinnable {
    fun spin(speed: Measure<Spin>, duration: Measure<Time>)
}
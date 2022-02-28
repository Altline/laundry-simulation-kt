package altline.things.spin

import altline.things.measure.Spin
import io.nacular.measured.units.*

interface Spinnable {
    fun spin(speed: Measure<Spin>, duration: Measure<Time>)
}
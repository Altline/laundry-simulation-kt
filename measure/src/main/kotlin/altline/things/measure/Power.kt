package altline.things.measure

import io.nacular.measured.units.*

class Power(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Power) = ratio / other.ratio

    companion object {
        val watts = Power("W")
        val kilowatts = Power("kW", 1000.0)
    }
}
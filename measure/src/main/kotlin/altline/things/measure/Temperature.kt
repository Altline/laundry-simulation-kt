package altline.things.measure

import io.nacular.measured.units.*

class Temperature(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Temperature) = ratio / other.ratio

    companion object {
        val celsius = Temperature("°C")
    }
}
package altline.appliance.measure

import io.nacular.measured.units.*

class Frequency(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Frequency) = ratio / other.ratio

    companion object {
        val hertz = Frequency("Hz")
    }
}
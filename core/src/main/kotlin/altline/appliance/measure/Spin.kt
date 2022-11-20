package altline.appliance.measure

import io.nacular.measured.units.*

class Spin(suffix: String, ratio: Double = 1.0) : Units(suffix, ratio) {
    operator fun div(other: Spin) = ratio / other.ratio

    companion object {
        val rpm = Spin("rpm")
    }
}
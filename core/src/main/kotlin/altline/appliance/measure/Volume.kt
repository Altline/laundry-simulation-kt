package altline.appliance.measure

import io.nacular.measured.units.*

class Volume(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Volume) = ratio / other.ratio

    companion object {
        val liters = Volume("l")
        val milliliters = Volume("ml", 0.001)
    }
}
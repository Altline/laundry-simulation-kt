package altline.things.measure

import altline.things.measure.Volume.Companion.liters
import io.nacular.measured.units.*

class Volume(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Volume) = ratio / other.ratio

    companion object {
        val liters = Volume("L")
        val milliliters = Volume("mL", 0.001)
        val deciliters = Volume("dL", 0.01)
    }
}
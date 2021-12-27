package altline.things.measure

import io.nacular.measured.units.Units

class Energy(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Volume) = ratio / other.ratio

    companion object {
        val joule = Volume("J")
    }
}
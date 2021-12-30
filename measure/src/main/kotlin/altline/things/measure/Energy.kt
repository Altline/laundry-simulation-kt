package altline.things.measure

import io.nacular.measured.units.Units

class Energy(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Energy) = ratio / other.ratio

    companion object {
        val joules = Energy("J")
    }
}
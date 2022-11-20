package altline.appliance.measure

import io.nacular.measured.units.*

class Energy(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Energy) = ratio / other.ratio

    companion object {
        val joules = Energy("J")
        val calories = Energy("cal", 4.184)
    }
}
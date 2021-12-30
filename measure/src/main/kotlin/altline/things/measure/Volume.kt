package altline.things.measure

import altline.things.measure.Volume.Companion.liters
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Units
import io.nacular.measured.units.times

class Volume(suffix: String, ratio: Double = 1.0): Units(suffix, ratio) {
    operator fun div(other: Volume) = ratio / other.ratio

    companion object {
        val liters = Volume("L")
        val milliliters = Volume("mL", 0.001)
        val deciliters = Volume("dL", 0.01)
    }
}

fun <T> Collection<T>.sumOf(selector: (T) -> Measure<Volume>): Measure<Volume> {
    var sum: Measure<Volume> = 0 * liters
    for (element in this) {
        sum += selector(element)
    }
    return sum
}
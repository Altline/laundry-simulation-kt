package altline.things.common

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Units

fun Double.isNegligible(threshold: Double): Boolean
    = this >= -threshold && this <= threshold

fun <T : Units> Measure<T>.isNegligible(threshold: Double = 0.00001): Boolean
    = amount.isNegligible(threshold)
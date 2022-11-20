package altline.appliance.util

import kotlin.math.abs

fun Double.isNegligible(threshold: Double): Boolean =
    this >= -threshold && this <= threshold

fun Double.isNotNegligible(threshold: Double) = !isNegligible(threshold)

fun Double.closeEquals(other: Double, threshold: Double): Boolean =
    abs(this - other) <= threshold
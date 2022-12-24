package altline.appliance.util

import kotlin.math.abs

fun Double.isNegligible(threshold: Double): Boolean =
    this >= -threshold && this <= threshold

fun Double.isNotNegligible(threshold: Double) = !isNegligible(threshold)

fun Double.closeEquals(other: Double, threshold: Double): Boolean =
    abs(this - other) <= threshold

fun Int.wrapAround(range: IntRange): Int {
    return when {
        this < range.first -> range.last
        this > range.last -> range.first
        else -> this
    }
}

/**
 * Linearly interpolate between [start] and [stop] with [fraction] fraction between them.
 * The returned value is always limited to the range between [start] and [stop].
 */
fun lerpCoerced(start: Float, stop: Float, fraction: Float): Float {
    val coercedFraction = fraction.coerceIn(0f, 1f)
    return ((1 - coercedFraction) * start + coercedFraction * stop)
}
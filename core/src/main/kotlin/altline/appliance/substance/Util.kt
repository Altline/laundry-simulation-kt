package altline.appliance.substance

import altline.appliance.measure.Volume
import altline.appliance.measure.isNegligible
import altline.appliance.transit.Flowable
import io.nacular.measured.units.*
import kotlin.math.pow

/** Calculates a nominal value of one part from the desired value of the mix */
fun calcNominal(
    desiredValue: Double,
    solventValue: Double,
    solventAmount: Measure<Volume>,
    soluteAmount: Measure<Volume>
): Double {
    val totalAmount = solventAmount + soluteAmount
    val solventPart = solventAmount / totalAmount
    val solutePart = soluteAmount / totalAmount
    return (desiredValue - solventPart * solventValue) / solutePart
}

fun <T : Units> Flowable<T>.isEmpty() = amount.isNegligible()
fun <T : Units> Flowable<T>.isNotEmpty() = !isEmpty()

/**
 * Calculates a measure amount that grows at the given [rate] as time measured by [seconds] passes, up to the [limit].
 */
fun <T : Units> calcGrowth(limit: Measure<T>, rate: Double, seconds: Double): Measure<T> {
    return limit - (limit * (1 - rate).pow(seconds))
}
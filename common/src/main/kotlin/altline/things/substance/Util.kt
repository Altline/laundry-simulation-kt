package altline.things.substance

import altline.things.measure.Volume
import altline.things.measure.isNegligible
import altline.things.transit.Flowable
import io.nacular.measured.units.*

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
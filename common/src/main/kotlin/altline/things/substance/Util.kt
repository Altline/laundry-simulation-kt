package altline.things.substance

import altline.things.measure.Volume
import io.nacular.measured.units.Measure
import io.nacular.measured.units.div

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
package altline.things.washing

import altline.things.measure.Volume
import altline.things.substance.CommonSubstanceTypes
import altline.things.substance.SubstanceType
import altline.things.substance.calcNominal
import io.nacular.measured.units.times

interface DetergentType : SubstanceType {
    /** The part of a basic stain substance able to be removed in one second of washing. */
    val cleaningPower: Double
}

enum class CommonDetergents(
    override val cleaningPower: Double
) : DetergentType {

    USELESS_DETERGENT(convertDiluted(0.0)),
    BARELY_DETERGENT(convertDiluted(0.0002)),
    WEAK_DETERGENT(convertDiluted(0.0003)),
    MILD_DETERGENT(convertDiluted(0.0005)),
    BASIC_DETERGENT(convertDiluted(0.001)),
    STRONG_DETERGENT(convertDiluted(0.002)),
    ULTIMATE_DETERGENT(convertDiluted(1.0));
}

/** Used to calculate the nominal cleaning power of a substance based on the desired cleaning power of the whole mix
 *  when diluted. */
private fun convertDiluted(desiredValue: Double) = calcNominal(
    desiredValue,
    solventValue = CommonSubstanceTypes.WATER.cleaningPower,
    solventAmount = 15.0 * Volume.liters,
    soluteAmount = 100.0 * Volume.milliliters
)
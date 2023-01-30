package altline.appliance.washing

import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.substance.CommonSubstanceTypes
import altline.appliance.substance.SubstanceConsistency
import altline.appliance.substance.SubstanceType
import altline.appliance.substance.calcNominal
import io.nacular.measured.units.*

interface DetergentType : SubstanceType {
    /** The part of a basic stain substance able to be removed in one second of washing. */
    val cleaningPower: Double
}

enum class CommonDetergents(
    override val cleaningPower: Double,
    override val consistency: SubstanceConsistency = SubstanceConsistency.ThickLiquid
) : DetergentType {

    ULTIMATE_DETERGENT(convertDiluted(1.0)),
    STRONG_DETERGENT(convertDiluted(0.002)),
    BASIC_DETERGENT(convertDiluted(0.001)),
    MILD_DETERGENT(convertDiluted(0.0005)),
    WEAK_DETERGENT(convertDiluted(0.0003)),
    BARELY_DETERGENT(convertDiluted(0.0002)),
    USELESS_DETERGENT(convertDiluted(0.0));

    override val evaporates: Boolean = false
}

/** Used to calculate the nominal cleaning power of a substance based on the desired cleaning power of the whole mix
 *  when diluted. */
private fun convertDiluted(desiredValue: Double) = calcNominal(
    desiredValue,
    solventValue = CommonSubstanceTypes.WATER.cleaningPower,
    solventAmount = 15.0 * liters,
    soluteAmount = 100.0 * milliliters
)
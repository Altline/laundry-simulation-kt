package altline.appliance.washing

import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.measure.sumOf
import altline.appliance.substance.CommonSubstanceTypes
import altline.appliance.substance.Substance
import altline.appliance.substance.SubstanceType
import altline.appliance.substance.calcNominal
import altline.appliance.washing.laundry.CommonFabricSofteners
import io.nacular.measured.units.*

/** The part of a basic stain substance able to be removed in one second of washing. */
val Substance.cleaningPower: Double
    get() = synchronized(this) {
        parts.cleaningPower
    }

/** The part of a basic stain substance able to be removed in one second of washing. */
val Set<Substance.Part>.cleaningPower: Double
    get() {
        val totalAmount = sumOf { it.amount }
        var power = 0.0
        forEach { part ->
            val ratio = part.amount / totalAmount
            val effectivePower = part.type.cleaningPower * ratio
            power += effectivePower
        }
        return power
    }

/** The part of a basic stain substance able to be removed in one second of washing. */
val SubstanceType.cleaningPower: Double
    get() = when (this) {
        is DetergentType -> cleaningPower

        CommonFabricSofteners.USELESS_SOFTENER -> 0.0
        CommonFabricSofteners.BARELY_SOFTENER -> convertDiluted(0.0005)
        CommonFabricSofteners.WEAK_SOFTENER -> convertDiluted(0.0006)
        CommonFabricSofteners.MILD_SOFTENER -> convertDiluted(0.0007)
        CommonFabricSofteners.BASIC_SOFTENER -> convertDiluted(0.0008)
        CommonFabricSofteners.STRONG_SOFTENER -> convertDiluted(0.0001)
        CommonFabricSofteners.ULTIMATE_SOFTENER -> convertDiluted(0.002)

        CommonSubstanceTypes.WATER -> 0.0005
        else -> 0.0
    }

private fun convertDiluted(desiredValue: Double) = calcNominal(
    desiredValue,
    solventValue = CommonSubstanceTypes.WATER.cleaningPower,
    solventAmount = 15.0 * liters,
    soluteAmount = 100.0 * milliliters
)
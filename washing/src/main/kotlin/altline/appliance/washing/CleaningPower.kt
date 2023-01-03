package altline.appliance.washing

import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.substance.*
import io.nacular.measured.units.*

/** The part of a basic stain substance able to be removed in one second of washing. */
val Substance.cleaningPower: Double
    get() {
        synchronized(this) {
            var power = 0.0
            parts.forEach { part ->
                val ratio = part.amount / amount
                val effectivePower = part.type.cleaningPower * ratio
                power += effectivePower
            }
            return power
        }
    }

val SubstanceType.cleaningPower: Double
    get() {
        return when (this) {
            is DetergentType -> cleaningPower

            CommonFabricSofteners.USELESS_SOFTENER -> 0.0
            CommonFabricSofteners.BARELY_SOFTENER -> convertDiluted(0.00005)
            CommonFabricSofteners.WEAK_SOFTENER -> convertDiluted(0.0001)
            CommonFabricSofteners.MILD_SOFTENER -> convertDiluted(0.00015)
            CommonFabricSofteners.BASIC_SOFTENER -> convertDiluted(0.0002)
            CommonFabricSofteners.STRONG_SOFTENER -> convertDiluted(0.00025)
            CommonFabricSofteners.ULTIMATE_SOFTENER -> convertDiluted(0.0003)

            CommonSubstanceTypes.WATER -> 0.0001
            else -> 0.0
        }
    }

private fun convertDiluted(desiredValue: Double) = calcNominal(
    desiredValue,
    solventValue = CommonSubstanceTypes.WATER.cleaningPower,
    solventAmount = 15.0 * liters,
    soluteAmount = 50.0 * milliliters
)
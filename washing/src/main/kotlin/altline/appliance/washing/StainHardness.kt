package altline.appliance.washing

import altline.appliance.substance.CommonSubstanceTypes
import altline.appliance.substance.Substance
import altline.appliance.substance.SubstanceType
import io.nacular.measured.units.*

/** The factor of a stain's resistance to being washed, where 0 means no resistance and 1 means complete unwashability. */
val Substance.stainHardness: Double
    get() = synchronized(this) {
        var hardness = 0.0
        parts.forEach { part ->
            val ratio = part.amount / amount
            val effectiveHardness = part.type.stainHardness * ratio
            hardness += effectiveHardness
        }
        return hardness
    }

/** The factor of a stain's resistance to being washed, where 0 means no resistance and 1 means complete unwashability. */
val SubstanceType.stainHardness: Double
    get() = when (this) {
        CommonSubstanceTypes.MUD -> 0.4
        CommonSubstanceTypes.COFFEE -> 0.6
        CommonSubstanceTypes.KETCHUP -> 0.2
        CommonSubstanceTypes.CRUDE_OIL -> 0.9
        else -> 0.0
    }

package altline.appliance.washing.laundry

import altline.appliance.substance.Substance
import altline.appliance.substance.SubstanceType
import io.nacular.measured.units.*

val Substance.fresheningPotential: Double
    get() = synchronized(this) {
        var potential = 0.0
        parts.forEach { part ->
            val ratio = part.amount / amount
            val effectivePower = part.type.fresheningPotential * ratio
            potential += effectivePower
        }
        return potential
    }

val SubstanceType.fresheningPotential: Double
    get() = when (this) {
        is FabricSoftenerType -> fresheningPotential
        else -> 0.0
    }
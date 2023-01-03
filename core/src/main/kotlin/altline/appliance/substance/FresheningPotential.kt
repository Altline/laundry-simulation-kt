package altline.appliance.substance

import io.nacular.measured.units.*

val Substance.fresheningPotential: Double
    get() {
        synchronized(this) {
            var potential = 0.0
            parts.forEach { part ->
                val ratio = part.amount / amount
                val effectivePower = part.type.fresheningPotential * ratio
                potential += effectivePower
            }
            return potential
        }
    }

val SubstanceType.fresheningPotential: Double
    get() {
        return when (this) {
            is FabricSoftenerType -> fresheningPotential
            else -> 0.0
        }
    }
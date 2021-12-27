package altline.things.substances

import altline.things.measure.Volume
import altline.things.measure.isNegligible
import altline.things.measure.sumOf
import io.nacular.measured.units.Measure
import io.nacular.measured.units.div

interface Substance {
    val parts: Set<Part>

    val totalAmount: Measure<Volume>
        get() = parts.sumOf { it.amount }

    val isEmpty: Boolean
        get() = parts.isEmpty()


    interface Part {
        val type: SubstanceType
        val amount: Measure<Volume>
    }

    companion object {
        fun Part(type: SubstanceType, amount: Measure<Volume>) = object : Part {
            override val type: SubstanceType = type
            override val amount: Measure<Volume> = amount

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (javaClass != other?.javaClass) return false

                other as Part

                if (type != other.type) return false

                return true
            }

            override fun hashCode(): Int {
                return type.hashCode()
            }
        }
    }
}

class MutableSubstance(
    parts: Set<Substance.Part>
) : Substance {
    constructor(type: SubstanceType, amount: Measure<Volume>)
            : this(setOf(Substance.Part(type, amount)))

    constructor() : this(emptySet())

    private val _parts = mutableSetOf(*parts.asMutableParts().toTypedArray())
    override val parts = _parts as Set<Substance.Part>

    fun add(other: Substance) {
        other.parts.forEach { part ->
            val existingPart = _parts.find { it.type == part.type }
            if (existingPart != null) {
                existingPart.amount += part.amount
            } else {
                _parts += MutablePart(part.type, part.amount)
            }
        }
    }

    fun separate(amount: Measure<Volume>): MutableSubstance {
        val ratio = (amount / totalAmount).coerceAtMost(1.0)
        val newParts = mutableSetOf<Substance.Part>()
        _parts.forEach { part ->
            val separatedAmount = part.amount * ratio
            part.amount -= separatedAmount
            newParts += Substance.Part(part.type, separatedAmount)
        }
        _parts.removeAll { it.amount.isNegligible() }
        return MutableSubstance(newParts)
    }

    private fun Substance.Part.asMutable() = MutablePart(type, amount)
    private fun Set<Substance.Part>.asMutableParts() = map { it.asMutable() }.toSet()

    private class MutablePart(
        override var type: SubstanceType,
        override var amount: Measure<Volume>
    ) : Substance.Part
}

fun substanceOf(parts: Set<Substance.Part>) = object : Substance {
    override val parts = setOf(*parts.toTypedArray())
}

fun substanceOf(type: SubstanceType, amount: Measure<Volume>) = object : Substance {
    override val parts = setOf(Substance.Part(type, amount))
}
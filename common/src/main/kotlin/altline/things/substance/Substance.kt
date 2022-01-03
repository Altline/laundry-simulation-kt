package altline.things.substance

import altline.things.measure.Volume
import altline.things.measure.isNegligible
import altline.things.measure.sumOf
import altline.things.transit.Flowable
import io.nacular.measured.units.*

class Substance(
    parts: Set<Part>
) : Flowable<Volume> {
    constructor(type: SubstanceType, amount: Measure<Volume>)
            : this(setOf(Part(type, amount)))

    constructor() : this(emptySet())

    private val _parts = mutableSetOf(*parts.filterEmpty().asMutableParts().toTypedArray())
    val parts = _parts as Set<Part>

    override val amount: Measure<Volume>
        get() = parts.sumOf { it.amount }

    val isEmpty: Boolean
        get() = parts.isEmpty()

    override fun add(other: Flowable<Volume>) {
        require(other is Substance)
        other.parts.forEach { part ->
            val existingPart = _parts.find { it.type == part.type }
            if (existingPart != null) {
                existingPart.amount += part.amount
            } else {
                _parts += MutablePart(part.type, part.amount)
            }
        }
    }

    override fun extract(amount: Measure<Volume>): Substance {
        val ratio = (amount / this.amount).coerceAtMost(1.0)
        val newParts = mutableSetOf<Part>()
        _parts.forEach { part ->
            val separatedAmount = part.amount * ratio
            part.amount -= separatedAmount
            newParts += Part(part.type, separatedAmount)
        }
        _parts.removeAll { it.amount.isNegligible() }
        return Substance(newParts)
    }

    fun extractAll(): Substance {
        return Substance(parts).also { _parts.clear() }
    }

    fun remixWith(other: Substance, amount: Measure<Volume>) {
        val thisPart = this.extract(amount)
        val otherPart = other.extract(amount)
        this.add(otherPart)
        other.add(thisPart)
    }

    private fun Part.asMutable() = MutablePart(type, amount)
    private fun Set<Part>.asMutableParts() = mapTo(mutableSetOf()) { it.asMutable() }

    private class MutablePart(
        override var type: SubstanceType,
        override var amount: Measure<Volume>
    ) : Part

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

private fun Set<Substance.Part>.filterEmpty(): Set<Substance.Part> {
    return filterTo(mutableSetOf()) { it.amount.amount >= 0.0 }
}
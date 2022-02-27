package altline.things.substance

import altline.things.measure.Volume
import altline.things.measure.isNegligible
import altline.things.measure.sumOf
import altline.things.transit.Flowable
import altline.things.transit.MutableFlowable
import io.nacular.measured.units.*

interface Substance : Flowable<Volume> {
    val parts: Set<Part>

    override val amount: Measure<Volume>
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
        }
    }
}

class MutableSubstance(
    parts: Set<Substance.Part>
) : Substance, MutableFlowable<Volume> {

    constructor(type: SubstanceType, amount: Measure<Volume>)
            : this(setOf(Substance.Part(type, amount)))

    constructor() : this(emptySet())

    private val _parts = mutableSetOf(*parts.filterEmpty().asMutableParts().toTypedArray())
    override val parts = _parts as Set<Substance.Part>

    override fun add(other: MutableFlowable<Volume>) {
        require(other is MutableSubstance)
        other.parts.forEach { part ->
            val existingPart = _parts.find { it.type == part.type }
            if (existingPart != null) {
                existingPart.amount += part.amount
            } else {
                _parts += MutablePart(part.type, part.amount)
            }
        }
        other._parts.clear()
    }

    override fun extract(amount: Measure<Volume>): MutableSubstance {
        val ratio = (amount / this.amount).coerceAtMost(1.0)
        val newParts = mutableSetOf<Substance.Part>()
        _parts.forEach { part ->
            val separatedAmount = part.amount * ratio
            part.amount -= separatedAmount
            newParts += Substance.Part(part.type, separatedAmount)
        }
        _parts.removeAll { it.amount.isNegligible() }
        return MutableSubstance(newParts)
    }

    override fun extractAll(): MutableSubstance {
        return MutableSubstance(parts).also { _parts.clear() }
    }

    fun remixWith(other: MutableSubstance, amount: Measure<Volume>) {
        val thisPart = this.extract(amount)
        val otherPart = other.extract(amount)
        this.add(otherPart)
        other.add(thisPart)
    }

    private fun Substance.Part.asMutable() = MutablePart(type, amount)
    private fun Set<Substance.Part>.asMutableParts() = mapTo(mutableSetOf()) { it.asMutable() }

    private class MutablePart(
        override var type: SubstanceType,
        override var amount: Measure<Volume>
    ) : Substance.Part
}

private fun Set<Substance.Part>.filterEmpty(): Set<Substance.Part> {
    return filterTo(mutableSetOf()) { it.amount.amount >= 0.0 }
}
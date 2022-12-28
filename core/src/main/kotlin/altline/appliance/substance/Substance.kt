package altline.appliance.substance

import altline.appliance.measure.Temperature
import altline.appliance.measure.Volume
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.isNegligible
import altline.appliance.measure.sumOf
import altline.appliance.transit.Flowable
import altline.appliance.transit.MutableFlowable
import io.nacular.measured.units.Measure
import io.nacular.measured.units.div
import io.nacular.measured.units.times

interface Substance : Flowable<Volume> {
    val parts: Set<Part>

    /** The temperature of the substance. This is null if, and only if, the substance is empty. */
    val temperature: Measure<Temperature>?

    override val amount: Measure<Volume>
        get() = parts.sumOf { it.amount }

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
    parts: Set<Substance.Part>,
    temperature: Measure<Temperature>?
) : Substance, MutableFlowable<Volume> {

    constructor(
        type: SubstanceType,
        amount: Measure<Volume>,
        temperature: Measure<Temperature>
    ) : this(setOf(Substance.Part(type, amount)), temperature)

    constructor() : this(emptySet(), null)

    override var temperature: Measure<Temperature>? = temperature
        set(value) {
            require(isEmpty() && value == null || isNotEmpty() && value != null) {
                "Temperature must be null if the substance is empty and must not be null if it is not empty.\n" +
                        "isEmpty: ${isEmpty()}, temperature: $value"
            }
            field = value
        }

    private val _parts = mutableSetOf(*parts.filterEmpty().asMutableParts().toTypedArray())
    override val parts = _parts as Set<Substance.Part>

    private val mutexLock = Any()

    override fun add(other: MutableFlowable<Volume>) = synchronized(mutexLock) {
        require(other is MutableSubstance)
        val newTemperature = mergeTemperature(other)
        other.parts.forEach { part ->
            val existingPart = _parts.find { it.type == part.type }
            if (existingPart != null) {
                existingPart.amount += part.amount
            } else {
                _parts += MutablePart(part.type, part.amount)
            }
        }
        other._parts.clear()
        this.temperature = newTemperature
    }

    private fun mergeTemperature(other: Substance): Measure<Temperature>? {
        return when {
            this.isEmpty() && other.isEmpty() -> null
            this.isEmpty() -> other.temperature
            other.isEmpty() -> this.temperature
            else -> {
                val totalAmount = this.amount + other.amount
                val thisRatio = this.amount / totalAmount
                val otherRatio = other.amount / totalAmount
                (this.temperature!! * thisRatio) + (other.temperature!! * otherRatio)
            }
        }
    }

    override fun extract(amount: Measure<Volume>): MutableSubstance = synchronized(mutexLock) {
        if (amount == 0 * liters) return MutableSubstance()

        val ratio = (amount / this.amount).coerceAtMost(1.0)
        val newParts = mutableSetOf<Substance.Part>()
        _parts.forEach { part ->
            val separatedAmount = part.amount * ratio
            part.amount -= separatedAmount
            newParts += Substance.Part(part.type, separatedAmount)
        }
        _parts.removeAll { it.amount.isNegligible() }
        return MutableSubstance(newParts, temperature).also {
            if (isEmpty()) temperature = null
        }
    }

    override fun extractAll(): MutableSubstance = synchronized(mutexLock) {
        return MutableSubstance(parts, temperature).also {
            _parts.clear()
            temperature = null
        }
    }

    fun remixWith(other: MutableSubstance, amount: Measure<Volume>) = synchronized(mutexLock) {
        if (amount == 0 * liters) return

        val realAmount = amount.coerceAtMost(minOf(this.amount, other.amount))
        val thisPart = this.extract(realAmount)
        val otherPart = other.extract(realAmount)
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
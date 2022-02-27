package altline.things.electricity

import altline.things.measure.Energy
import altline.things.measure.Energy.Companion.joules
import altline.things.transit.Flowable
import altline.things.transit.MutableFlowable
import io.nacular.measured.units.*

interface ElectricalEnergy : Flowable<Energy>

class MutableElectricalEnergy(
    amount: Measure<Energy>
) : ElectricalEnergy, MutableFlowable<Energy> {

    constructor() : this(0.0 * joules)

    override var amount: Measure<Energy> = amount
        private set

    override fun add(other: MutableFlowable<Energy>) {
        require(other is MutableElectricalEnergy)
        amount += other.amount
        other.amount = 0.0 * joules
    }

    override fun extract(amount: Measure<Energy>): MutableFlowable<Energy> {
        this.amount -= amount
        return MutableElectricalEnergy(amount)
    }

    override fun extractAll(): MutableFlowable<Energy> {
        return MutableElectricalEnergy(amount).also { amount = 0.0 * joules }
    }
}
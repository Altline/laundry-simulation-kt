package altline.appliance.washing.laundry

import altline.appliance.measure.Volume
import altline.appliance.measure.VolumetricFlow
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.Substance
import altline.appliance.substance.transit.BasicSubstanceConduit
import altline.appliance.substance.transit.SubstanceConduit
import altline.appliance.substance.transit.Valve
import altline.appliance.washing.WashDispenser
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time

abstract class SlottedDispenser(
    protected val config: LaundryWasherConfig
) : LaundryWashDispenser {

    abstract val tray: Tray
    protected abstract val solventInputJunction: SubstanceConduit
    protected abstract val solventOutputJunction: SubstanceConduit

    override val inputPort by lazy { solventInputJunction.inputs[0] }
    override val outputPort by lazy { solventOutputJunction.outputs[0] }

    private var isTrayClosed = true

    fun openTray() {
        isTrayClosed = false
        refreshTrayConnections()
    }

    fun closeTray() {
        isTrayClosed = true
        refreshTrayConnections()
    }

    protected abstract fun refreshTrayConnections()

    protected inner class Channel(
        private val index: Int
    ) : WashDispenser.Channel {
        private val valve = Valve(config.intakeFlowRate)

        init {
            solventInputJunction.outputs[index] connectTo valve.inputs[0]
            refreshTrayConnections()
        }

        fun refreshTrayConnections() {
            if (isTrayClosed) {
                val correspondingSlot = tray.getSlot(index)
                valve.outputs[0] connectTo correspondingSlot.inputs[0]
                correspondingSlot.outputs[0] connectTo solventOutputJunction.inputs[index]
            } else {
                valve.outputs[0] connectTo solventOutputJunction.inputs[index]
            }
        }

        override fun dispense() {
            valve.open()
        }

        override fun halt() {
            valve.close()
        }
    }

    interface Tray {
        fun getSlot(index: Int): Slot
        fun empty(): MutableSubstance

        class Slot(
            val capacity: Measure<Volume>,
            maxFlowRate: Measure<VolumetricFlow>
        ) : BasicSubstanceConduit(maxFlowRate) {

            private val _additive = MutableSubstance()
            val additive = _additive as Substance

            fun fill(additive: MutableSubstance) {
                val amountToAdd = capacity - this._additive.amount
                val toAdd = additive.extract(amountToAdd)
                this._additive.add(toAdd)
            }

            fun empty(amount: Measure<Volume> = capacity): MutableSubstance {
                return this._additive.extract(amount)
            }

            override fun pushFlow(flowable: MutableSubstance, timeFrame: Measure<Time>, flowId: Long): Measure<Volume> {
                flowable.add(_additive.extractAll())
                return super.pushFlow(flowable, timeFrame, flowId)
            }

            override fun pullFlow(amount: Measure<Volume>, timeFrame: Measure<Time>, flowId: Long): MutableSubstance? {
                return null
            }
        }
    }
}
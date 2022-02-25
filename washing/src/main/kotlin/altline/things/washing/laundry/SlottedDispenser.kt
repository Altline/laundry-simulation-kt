package altline.things.washing.laundry

import altline.things.measure.Volume
import altline.things.substance.MutableSubstance
import altline.things.substance.transit.*
import altline.things.washing.WashDispenser
import io.nacular.measured.units.*

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
            maxFlowRate: Measure<UnitsRatio<Volume, Time>>
        ) : BasicSubstanceConduit(maxFlowRate) {

            private val additive = MutableSubstance()

            fun fill(additive: MutableSubstance) {
                val amountToAdd = capacity - this.additive.amount
                val toAdd = additive.extract(amountToAdd)
                this.additive.add(toAdd)
            }

            fun empty(): MutableSubstance {
                return this.additive.extractAll()
            }

            override fun pushFlow(flowable: MutableSubstance, timeFrame: Measure<Time>, flowId: Long): Measure<Volume> {
                flowable.add(additive.extractAll())
                return super.pushFlow(flowable, timeFrame, flowId)
            }

            override fun pullFlow(amount: Measure<Volume>, timeFrame: Measure<Time>, flowId: Long): MutableSubstance? {
                return null
            }
        }
    }
}
package altline.appliance.washing.laundry

import altline.appliance.measure.Volume
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.transit.BasicSubstanceConduit
import io.nacular.measured.units.*

class PreWashSlottedDispenser(
    preWashDetergentCapacity: Measure<Volume>,
    mainDetergentCapacity: Measure<Volume>,
    mainSoftenerCapacity: Measure<Volume>,
    config: LaundryWasherConfig
) : SlottedDispenser(config) {

    override val tray = Tray(preWashDetergentCapacity, mainDetergentCapacity, mainSoftenerCapacity, config)
    override val solventInputJunction = BasicSubstanceConduit(config.intakeFlowRate, 1, 3)
    override val solventOutputJunction = BasicSubstanceConduit(config.intakeFlowRate, 3, 1)

    private val preWashDetergentChannel = Channel(0)
    private val mainDetergentChannel = Channel(1)
    private val mainSoftenerChannel = Channel(2)

    override fun refreshTrayConnections() {
        preWashDetergentChannel.refreshTrayConnections()
        mainDetergentChannel.refreshTrayConnections()
        mainSoftenerChannel.refreshTrayConnections()
    }

    fun dispensePreWashDetergent() {
        preWashDetergentChannel.dispense()
    }

    fun haltPreWashDetergent() {
        preWashDetergentChannel.halt()
    }

    override fun dispenseMainDetergent() {
        mainDetergentChannel.dispense()
    }

    override fun haltMainDetergent() {
        mainDetergentChannel.halt()
    }

    override fun dispenseMainSoftener() {
        mainSoftenerChannel.dispense()
    }

    override fun haltMainSoftener() {
        mainSoftenerChannel.halt()
    }

    class Tray(
        preWashDetergentCapacity: Measure<Volume>,
        mainDetergentCapacity: Measure<Volume>,
        mainSoftenerCapacity: Measure<Volume>,
        config: LaundryWasherConfig
    ) : SlottedDispenser.Tray {

        val preWashDetergentSlot = SlottedDispenser.Tray.Slot(preWashDetergentCapacity, config.intakeFlowRate)
        val mainDetergentSlot = SlottedDispenser.Tray.Slot(mainDetergentCapacity, config.intakeFlowRate)
        val mainSoftenerSlot = SlottedDispenser.Tray.Slot(mainSoftenerCapacity, config.intakeFlowRate)

        override fun getSlot(index: Int): SlottedDispenser.Tray.Slot {
            return when (index) {
                0 -> preWashDetergentSlot
                1 -> mainDetergentSlot
                2 -> mainSoftenerSlot
                else -> throw IndexOutOfBoundsException(index)
            }
        }

        override fun empty(): MutableSubstance {
            return MutableSubstance().apply {
                add(preWashDetergentSlot.empty())
                add(mainDetergentSlot.empty())
                add(mainSoftenerSlot.empty())
            }
        }
    }
}
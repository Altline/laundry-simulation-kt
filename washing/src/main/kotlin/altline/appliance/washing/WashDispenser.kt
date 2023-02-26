package altline.appliance.washing

import altline.appliance.substance.transit.SubstanceDrainPort
import altline.appliance.substance.transit.SubstanceSourcePort

interface WashDispenser {
    val inputPort: SubstanceDrainPort
    val outputPort: SubstanceSourcePort

    val isDispensingMainDetergent: Boolean

    fun dispenseMainDetergent()
    fun haltMainDetergent()

    interface Channel {
        val isDispensing: Boolean

        fun dispense()
        fun halt()
    }
}
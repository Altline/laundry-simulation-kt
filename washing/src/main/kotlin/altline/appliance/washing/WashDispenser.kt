package altline.appliance.washing

import altline.appliance.substance.transit.SubstanceDrainPort
import altline.appliance.substance.transit.SubstanceSourcePort

interface WashDispenser {
    val inputPort: SubstanceDrainPort
    val outputPort: SubstanceSourcePort

    fun dispenseMainDetergent()
    fun haltMainDetergent()

    interface Channel {
        fun dispense()
        fun halt()
    }
}
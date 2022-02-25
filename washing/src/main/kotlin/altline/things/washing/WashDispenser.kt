package altline.things.washing

import altline.things.substance.transit.SubstanceDrainPort
import altline.things.substance.transit.SubstanceSourcePort

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
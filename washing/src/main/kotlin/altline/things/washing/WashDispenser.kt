package altline.things.washing

import altline.things.substance.transit.SubstanceDrainPort
import altline.things.substance.transit.SubstanceSourcePort

interface WashDispenser {
    fun connectSolventSource(source: SubstanceSourcePort)
    fun disconnectSolventSource()

    fun connectDrain(drain: SubstanceDrainPort)
    fun disconnectDrain()

    fun dispenseMainDetergent()
    fun haltMainDetergent()

    interface Channel {
        fun dispense()
        fun halt()
    }
}
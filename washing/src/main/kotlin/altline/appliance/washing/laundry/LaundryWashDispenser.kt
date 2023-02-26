package altline.appliance.washing.laundry

import altline.appliance.washing.WashDispenser

interface LaundryWashDispenser : WashDispenser {
    val isDispensingMainSoftener: Boolean

    fun dispenseMainSoftener()
    fun haltMainSoftener()
}
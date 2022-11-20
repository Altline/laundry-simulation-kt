package altline.appliance.washing.laundry

import altline.appliance.washing.WashDispenser

interface LaundryWashDispenser : WashDispenser {
    fun dispenseMainSoftener()
    fun haltMainSoftener()
}
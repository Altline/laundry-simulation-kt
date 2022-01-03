package altline.things.washing.laundry

import altline.things.washing.WashDispenser

interface LaundryWashDispenser : WashDispenser {
    fun dispenseMainSoftener()
    fun haltMainSoftener()
}
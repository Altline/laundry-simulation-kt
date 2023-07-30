package altline.appliance.substance

import altline.appliance.common.Body
import altline.appliance.measure.Volume
import io.nacular.measured.units.*

interface SoakableBody: Body {
    val soakedSubstance: Substance
    val soakRatio: Double

    fun soak(substance: MutableSubstance)
    fun resoakWith(substance: MutableSubstance, amount: Measure<Volume>)
    fun dry(amount: Measure<Volume>, stainNonEvaporatingParts: Boolean): MutableSubstance
}
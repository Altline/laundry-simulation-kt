package altline.things.substance.transit

import altline.things.measure.Volume
import altline.things.measure.Volume.Companion.liters
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class Valve(
    maxFlowRate: Measure<UnitsRatio<Volume, Time>>
) : BasicSubstanceConduit(maxFlowRate) {

    fun open(flowRate: Measure<UnitsRatio<Volume, Time>> = maxFlowRate) {
        realFlowRate = flowRate.coerceIn(0.0 * liters / seconds, maxFlowRate)
    }

    fun close() {
        realFlowRate = 0.0 * liters / seconds
    }
}
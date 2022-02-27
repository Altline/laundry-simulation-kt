package altline.things.substance.transit

import altline.things.measure.Volume.Companion.liters
import altline.things.measure.VolumetricFlow
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class Valve(
    maxFlowRate: Measure<VolumetricFlow>
) : BasicSubstanceConduit(maxFlowRate) {

    fun open(flowRate: Measure<VolumetricFlow> = maxOutputFlowRate) {
        realOutputFlowRate = flowRate.coerceIn(0.0 * liters / seconds, maxOutputFlowRate)
    }

    fun close() {
        realOutputFlowRate = 0.0 * liters / seconds
    }
}
package altline.appliance.substance.transit

import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.VolumetricFlow
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class Valve(
    maxFlowRate: Measure<VolumetricFlow>,
    inputCount: Int = 1,
    outputCount: Int = 1
) : BasicSubstanceConduit(maxFlowRate, inputCount, outputCount) {

    val maxFlowRate: Measure<VolumetricFlow>
        get() = minOf(maxInputFlowRate, maxOutputFlowRate)

    var realFlowRate: Measure<VolumetricFlow>
        get() = minOf(realInputFlowRate, realOutputFlowRate)
        private set(value) {
            realInputFlowRate = value
            realOutputFlowRate = value
        }

    fun open(flowRate: Measure<VolumetricFlow> = maxFlowRate) {
        realFlowRate = flowRate.coerceIn(0.0 * liters / seconds, maxFlowRate)
    }

    fun close() {
        realFlowRate = 0.0 * liters / seconds
    }
}
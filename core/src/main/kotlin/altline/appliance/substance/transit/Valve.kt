package altline.appliance.substance.transit

import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.VolumetricFlow
import altline.appliance.measure.isNegligible
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class Valve(
    maxFlowRate: Measure<VolumetricFlow>,
    inputCount: Int = 1,
    outputCount: Int = 1
) : BasicSubstanceConduit(maxFlowRate, inputCount, outputCount) {

    override var realInputFlowRate = 0.0 * liters / seconds
    override var realOutputFlowRate = 0.0 * liters / seconds

    val maxFlowRate: Measure<VolumetricFlow>
        get() = minOf(maxInputFlowRate, maxOutputFlowRate)

    var realFlowRate: Measure<VolumetricFlow>
        get() = minOf(realInputFlowRate, realOutputFlowRate)
        private set(value) {
            realInputFlowRate = value
            realOutputFlowRate = value
        }

    val isClosed: Boolean
        get() = realFlowRate.isNegligible()

    fun open(flowRate: Measure<VolumetricFlow> = maxFlowRate) {
        realFlowRate = flowRate.coerceIn(0.0 * liters / seconds, maxFlowRate)
    }

    fun close() {
        realFlowRate = 0.0 * liters / seconds
    }
}
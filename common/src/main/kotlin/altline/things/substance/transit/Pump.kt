package altline.things.substance.transit

import altline.things.measure.Volume
import io.nacular.measured.units.*
import kotlin.random.Random

open class Pump(
    maxFlowRate: Measure<UnitsRatio<Volume, Time>>,
    inputCount: Int = 1,
    outputCount: Int = 1
) : BasicSubstanceConduit(maxFlowRate, inputCount, outputCount) {

    val maxFlowRate: Measure<UnitsRatio<Volume, Time>>
        get() = minOf(maxInputFlowRate, maxOutputFlowRate)

    var realFlowRate: Measure<UnitsRatio<Volume, Time>>
        get() = minOf(realInputFlowRate, realOutputFlowRate)
        protected set(value) {
            realInputFlowRate = value
            realOutputFlowRate = value
        }

    fun pump(timeFrame: Measure<Time>) {
        val amount = realFlowRate * timeFrame
        pump(amount, timeFrame)
    }

    protected fun pump(amount: Measure<Volume>, timeFrame: Measure<Time>) {
        val chunk = pullFlow(amount, timeFrame, Random.nextLong())
        if (chunk != null) {
            pushFlow(chunk, timeFrame, Random.nextLong())
        }
    }
}
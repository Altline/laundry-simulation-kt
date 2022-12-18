package altline.appliance.substance.transit

import altline.appliance.measure.Volume
import altline.appliance.measure.VolumetricFlow
import altline.appliance.measure.repeatPeriodically
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.isNotEmpty
import altline.appliance.transit.DefaultFlowTimeFrame
import altline.appliance.util.CoroutineManager
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random

open class Reservoir(
    val capacity: Measure<Volume>,
    val outflowThreshold: Measure<Volume>,
    shouldSpontaneouslyPush: Boolean,
    inputFlowRate: Measure<VolumetricFlow>,
    outputFlowRate: Measure<VolumetricFlow>,
    initialSubstance: MutableSubstance? = null
) : BasicSubstanceConduit(inputFlowRate, outputFlowRate) {

    protected open val storedSubstance = MutableSubstance().apply {
        if (initialSubstance != null) add(initialSubstance)
    }

    private val coroutineManager by lazy {
        CoroutineManager(CoroutineScope(Dispatchers.Default)) {
            spontaneousPushLoop()
        }
    }

    var spontaneouslyPushing: Boolean
        get() = coroutineManager.active
        protected set(value) {
            coroutineManager.active = value
        }

    init {
        require(outflowThreshold <= capacity)

        if (outflowThreshold != capacity) {
            spontaneouslyPushing = shouldSpontaneouslyPush
        }
    }

    val storedSubstanceAmount: Measure<Volume>
        get() = storedSubstance.amount

    private val freeSpace: Measure<Volume>
        get() = capacity - storedSubstanceAmount

    private val spaceBelowOutflow: Measure<Volume>
        get() = outflowThreshold - storedSubstanceAmount

    private val excessAmount: Measure<Volume>
        get() = storedSubstanceAmount - outflowThreshold

    override fun pushFlow(flowable: MutableSubstance, timeFrame: Measure<Time>, flowId: Long): Measure<Volume> {
        if (spaceBelowOutflow.amount > 0.0) {
            val flowableAmount = realInputFlowRate * timeFrame
            val amountToPush = spaceBelowOutflow.coerceAtMost(flowableAmount)
            val toStore = flowable.extract(amountToPush)
            storedSubstance.add(toStore)
        }

        if (flowable.isNotEmpty()) {
            super.pushFlow(flowable, timeFrame, flowId)
            val toStore = flowable.extract(freeSpace)
            storedSubstance.add(toStore)
        }

        return flowable.amount
    }

    override fun pullFlow(amount: Measure<Volume>, timeFrame: Measure<Time>, flowId: Long): MutableSubstance? {
        if (excessAmount.amount > 0.0) {
            val flowableAmount = realOutputFlowRate * timeFrame
            val toExtract = amount.coerceAtMost(flowableAmount).coerceAtMost(excessAmount)
            return storedSubstance.extract(toExtract)
        }
        return null
    }

    private suspend fun spontaneousPushLoop() {
        repeatPeriodically(DefaultFlowTimeFrame) {
            if (excessAmount.amount > 0.0) {
                val excess = storedSubstance.extract(excessAmount)
                super.pushFlow(excess, DefaultFlowTimeFrame, Random.nextLong())
            }
        }
    }
}
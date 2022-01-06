package altline.things.transit

import altline.things.measure.*
import altline.things.substance.isNotEmpty
import io.nacular.measured.units.*

open class BasicConduit<QuantityType : Units, FlowableType : Flowable<QuantityType>>(
    final override val maxFlowRate: Measure<UnitsRatio<QuantityType, Time>>,
    final override val inputCount: Int = 1,
    final override val outputCount: Int = 1
) : Conduit<QuantityType, FlowableType> {

    init {
        require(inputCount > 0)
        require(outputCount > 0)
    }

    override var realFlowRate = maxFlowRate
        protected set

    override val inputs = Array(inputCount) { FlowDrain.Port(this) }
    override val outputs = Array(outputCount) { FlowSource.Port(this) }

    private val connectedSources: List<FlowSource<QuantityType, FlowableType>>
        get() = inputs.mapNotNull { port -> port.connectedPort?.owner }

    private val connectedDrains: List<FlowDrain<QuantityType, FlowableType>>
        get() = outputs.mapNotNull { port -> port.connectedPort?.owner }

    private val seenIDs = mutableListOf<Long>()

    fun getSource(index: Int) = inputs[index].connectedPort?.owner
    fun getDrain(index: Int) = outputs[index].connectedPort?.owner

    @Suppress("UNCHECKED_CAST")
    override fun pushFlow(flowable: FlowableType, timeFrame: Measure<Time>, flowId: Long): Measure<QuantityType> {
        if (checkId(flowId)) {
            val pushableAmount = realFlowRate * timeFrame
            val amountToPush = flowable.amount.coerceAtMost(pushableAmount)
            val chunk = flowable.extract(amountToPush) as FlowableType
            tryPush(connectedDrains, chunk, timeFrame, flowId)
            flowable.add(chunk)
        }
        return flowable.amount
    }

    @Suppress("UNCHECKED_CAST")
    private fun tryPush(
        drains: List<FlowDrain<QuantityType, FlowableType>>,
        toPush: FlowableType,
        timeFrame: Measure<Time>,
        flowId: Long
    ) {
        val nonSpillingDrains = mutableListOf<FlowDrain<QuantityType, FlowableType>>()
        val totalOutputFlowRate = drains.sumOf { it.realFlowRate }
        drains.forEach { drain ->
            val ratio = drain.realFlowRate.divSameUnit(totalOutputFlowRate)
            val splitAmount = toPush.amount * ratio
            val chunk = toPush.extract(splitAmount) as FlowableType
            val leftoverAmount = drain.pushFlow(chunk, timeFrame, flowId)
            toPush.add(chunk)

            if (leftoverAmount.isNegligible())
                nonSpillingDrains += drain
        }

        if (toPush.isNotEmpty() && nonSpillingDrains.isNotEmpty())
            tryPush(nonSpillingDrains, toPush, timeFrame, flowId)
    }

    override fun pullFlow(amount: Measure<QuantityType>, timeFrame: Measure<Time>, flowId: Long): FlowableType? {
        if (checkId(flowId)) {
            val pullableAmount = realFlowRate * timeFrame
            val amountToPull = amount.coerceAtMost(pullableAmount)
            return tryPull(connectedSources, amountToPull, timeFrame, flowId)
        }
        return null
    }

    private fun tryPull(
        sources: List<FlowSource<QuantityType, FlowableType>>,
        amount: Measure<QuantityType>,
        timeFrame: Measure<Time>,
        flowId: Long
    ): FlowableType? {
        val unexhaustedSources = mutableListOf<FlowSource<QuantityType, FlowableType>>()
        var pulled: FlowableType? = null
        val totalInputFlowRate = sources.sumOf { it.realFlowRate }
        sources.forEach { source ->
            val ratio = source.realFlowRate.divSameUnit(totalInputFlowRate)
            val splitAmount = amount * ratio
            source.pullFlow(splitAmount, timeFrame, flowId)?.let { chunk ->
                if (pulled == null) pulled = chunk
                else pulled!!.add(chunk)

                if (chunk.amount.closeEquals(splitAmount)) {
                    unexhaustedSources += source
                }
            }
        }

        val leftToPull = if (pulled == null) amount else amount - pulled!!.amount
        if (leftToPull.isNotNegligible() && unexhaustedSources.isNotEmpty()){
            tryPull(unexhaustedSources, leftToPull, timeFrame, flowId)?.let { chunk ->
                pulled!!.add(chunk)
            }
        }

        return pulled
    }

    protected fun checkId(flowId: Long): Boolean {
        seenIDs.contains(flowId).let { seen ->
            if (seen) seenIDs.remove(flowId)
            else seenIDs.add(flowId)
            return !seen
        }
    }
}
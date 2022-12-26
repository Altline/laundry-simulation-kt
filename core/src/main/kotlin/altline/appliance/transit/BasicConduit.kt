package altline.appliance.transit

import altline.appliance.measure.*
import altline.appliance.substance.isNotEmpty
import io.nacular.measured.units.*

open class BasicConduit<QuantityType : Units, FlowableType : MutableFlowable<QuantityType>>(
    final override val maxInputFlowRate: Measure<UnitsRatio<QuantityType, Time>>,
    final override val maxOutputFlowRate: Measure<UnitsRatio<QuantityType, Time>>,
    final override val inputCount: Int = 1,
    final override val outputCount: Int = 1
) : Conduit<QuantityType, FlowableType> {

    constructor(
        maxFlowRate: Measure<UnitsRatio<QuantityType, Time>>,
        inputCount: Int = 1,
        outputCount: Int = 1
    ) : this(maxFlowRate, maxFlowRate, inputCount, outputCount)

    init {
        require(inputCount > 0)
        require(outputCount > 0)
    }

    override var realInputFlowRate = maxInputFlowRate
        protected set

    override var realOutputFlowRate = maxOutputFlowRate
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
            val flowableAmount = realOutputFlowRate * timeFrame
            val chunk = flowable.extract(flowableAmount) as FlowableType
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
        val totalOutputFlowRate = drains.sumOfOrNull { it.realInputFlowRate }.takeIf { it?.amount != 0.0 } ?: return

        drains.forEach { drain ->
            val ratio = drain.realInputFlowRate.divSameUnit(totalOutputFlowRate)
            val splitAmount = toPush.amount * ratio
            if (splitAmount.isNegligible()) return@forEach

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
            val flowableAmount = realInputFlowRate * timeFrame
            val askingAmount = flowableAmount.coerceAtMost(amount)
            return tryPull(connectedSources, askingAmount, timeFrame, flowId)
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
        val totalInputFlowRate = sources.sumOfOrNull { it.realOutputFlowRate }.takeIf { it?.amount != 0.0 } ?: return null

        sources.forEach { source ->
            val ratio = source.realOutputFlowRate.divSameUnit(totalInputFlowRate)
            val splitAmount = amount * ratio
            if (splitAmount.isNegligible()) return@forEach

            source.pullFlow(splitAmount, timeFrame, flowId)?.let { chunk ->
                if (pulled == null) pulled = chunk
                else pulled!!.add(chunk)

                if (chunk.amount.closeEquals(splitAmount)) {
                    unexhaustedSources += source
                }
            }
        }

        val leftToPull = if (pulled == null) amount else amount - pulled!!.amount
        if (leftToPull.isNotNegligible() && unexhaustedSources.isNotEmpty()) {
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
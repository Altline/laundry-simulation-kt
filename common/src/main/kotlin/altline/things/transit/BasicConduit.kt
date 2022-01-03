package altline.things.transit

import io.nacular.measured.units.*

open class BasicConduit<QuantityType : Units, FlowableType : Flowable<QuantityType>>(
    override val maxFlowRate: Measure<UnitsRatio<QuantityType, Time>>,
    final override val inputCount: Int = 1,
    final override val outputCount: Int = 1
) : Conduit<QuantityType, FlowableType> {

    init {
        require(inputCount > 0)
        require(outputCount > 0)
    }

    private val seenIDs = mutableListOf<Long>()

    override val inputs = Array(inputCount) { FlowDrain.Port(this) }
    override val outputs = Array(outputCount) { FlowSource.Port(this) }

    private val connectedSources: List<FlowSource<QuantityType, FlowableType>>
        get() = inputs.mapNotNull { port -> port.connectedPort?.owner }

    private val connectedDrains: List<FlowDrain<QuantityType, FlowableType>>
        get() = outputs.mapNotNull { port -> port.connectedPort?.owner }

    fun getSource(index: Int) = inputs[index].connectedPort?.owner
    fun getDrain(index: Int) = outputs[index].connectedPort?.owner

    @Suppress("UNCHECKED_CAST")
    override fun pushFlow(flowable: FlowableType, timeFrame: Measure<Time>, flowId: Long): Measure<QuantityType> {
        var leftoverAmount = (0.0 * flowable.amount)
        if (checkId(flowId)) {
            val connectedDrains = connectedDrains
            val splitAmount = flowable.amount / connectedDrains.size
            connectedDrains.forEach {
                val chunk = flowable.extract(splitAmount) as FlowableType
                leftoverAmount += it.pushFlow(chunk, timeFrame, flowId)
            }
        }
        return leftoverAmount
    }

    override fun pullFlow(amount: Measure<QuantityType>, timeFrame: Measure<Time>, flowId: Long): FlowableType? {
        var pulled: FlowableType? = null
        if (checkId(flowId)) {
            val connectedSources = connectedSources
            val splitAmount = amount / connectedSources.size
            connectedSources.forEach {
                it.pullFlow(splitAmount, timeFrame, flowId)?.let { chunk ->
                    if (pulled == null) pulled = chunk
                    else pulled!!.add(chunk)
                }
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
package altline.things.transit

import io.nacular.measured.units.*

open class BasicConduit<QuantityType : Units, FlowableType : Flowable<QuantityType>>(
    override val maxFlowRate: Measure<UnitsRatio<QuantityType, Time>>
) : Conduit<QuantityType, FlowableType> {

    private val seenIDs = mutableListOf<Long>()

    override var flowSource: FlowSource<QuantityType, FlowableType>? = null
        protected set

    override var flowDrain: FlowDrain<QuantityType, FlowableType>? = null
        protected set

    override fun connectFlowSource(source: FlowSource<QuantityType, FlowableType>) {
        if (this.flowSource == source) return
        if (this.flowSource != null) disconnectFlowSource()
        this.flowSource = source
        source.connectFlowDrain(this)
    }

    override fun disconnectFlowSource() {
        this.flowSource?.let {
            this.flowSource = null
            it.disconnectFlowDrain()
        }
    }

    override fun connectFlowDrain(drain: FlowDrain<QuantityType, FlowableType>) {
        if (this.flowDrain == drain) return
        if (this.flowDrain != null) disconnectFlowDrain()
        this.flowDrain = drain
        drain.connectFlowSource(this)
    }

    override fun disconnectFlowDrain() {
        this.flowDrain?.let {
            this.flowDrain = null
            it.disconnectFlowSource()
        }
    }

    override fun pushFlow(flowable: FlowableType, timeFrame: Measure<Time>, flowId: Long): Measure<QuantityType> {
        return flowDrain?.let {
            if (checkId(flowId)) {
                it.pushFlow(flowable, timeFrame, flowId)
            } else null
        } ?: (0.0 * flowable.amount)
    }

    override fun pullFlow(amount: Measure<QuantityType>, timeFrame: Measure<Time>, flowId: Long): FlowableType? {
        return flowSource?.let {
            if (checkId(flowId)) {
                it.pullFlow(amount, timeFrame, flowId)
            } else null
        }
    }

    protected fun checkId(flowId: Long): Boolean {
        seenIDs.contains(flowId).let { seen ->
            if (seen) seenIDs.remove(flowId)
            else seenIDs.add(flowId)
            return !seen
        }
    }
}
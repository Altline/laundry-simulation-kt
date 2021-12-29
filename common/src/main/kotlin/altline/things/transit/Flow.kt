package altline.things.transit

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.Units
import io.nacular.measured.units.UnitsRatio

interface Flowable<QuantityType : Units> {
    val amount: Measure<QuantityType>
}

interface FlowSource<QuantityType : Units, FlowableType : Flowable<QuantityType>> {
    val maxFlowRate: Measure<UnitsRatio<QuantityType, Time>>
    val flowDrain: FlowDrain<QuantityType, FlowableType>?

    fun connectFlowDrain(drain: FlowDrain<QuantityType, FlowableType>)
    fun disconnectFlowDrain()

    fun pullFlow(amount: Measure<QuantityType>, timeFrame: Measure<Time>, flowId: Long): FlowableType?
}

interface FlowDrain<QuantityType : Units, FlowableType : Flowable<QuantityType>> {
    val maxFlowRate: Measure<UnitsRatio<QuantityType, Time>>
    val flowSource: FlowSource<QuantityType, FlowableType>?

    fun connectFlowSource(source: FlowSource<QuantityType, FlowableType>)
    fun disconnectFlowSource()

    fun pushFlow(flowable: FlowableType, timeFrame: Measure<Time>, flowId: Long): Measure<QuantityType>
}

interface Conduit<QuantityType : Units, FlowableType : Flowable<QuantityType>>
    : FlowSource<QuantityType, FlowableType>, FlowDrain<QuantityType, FlowableType>
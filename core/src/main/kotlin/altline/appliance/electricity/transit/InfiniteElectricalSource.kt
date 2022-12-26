package altline.appliance.electricity.transit

import altline.appliance.electricity.MutableElectricalEnergy
import altline.appliance.measure.Energy
import altline.appliance.transit.FlowSource
import io.nacular.measured.units.*

class InfiniteElectricalSource(
    override val maxOutputFlowRate: Measure<UnitsRatio<Energy, Time>>,
    override val outputCount: Int = 1
) : ElectricalSource {

    override val realOutputFlowRate: Measure<UnitsRatio<Energy, Time>> = maxOutputFlowRate

    override val outputs = Array(outputCount) { FlowSource.Port(this) }

    override fun pullFlow(amount: Measure<Energy>, timeFrame: Measure<Time>, flowId: Long): MutableElectricalEnergy {
        return MutableElectricalEnergy(amount)
    }
}
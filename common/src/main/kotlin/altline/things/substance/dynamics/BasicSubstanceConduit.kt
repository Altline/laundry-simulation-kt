package altline.things.substance.dynamics

import altline.things.measure.Volume
import altline.things.substance.MutableSubstance
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.UnitsRatio
import io.nacular.measured.units.times

open class BasicSubstanceConduit(
    override val maxFlowRate: Measure<UnitsRatio<Volume, Time>>
) : SubstanceConduit {

    private val seenIDs = mutableListOf<Long>()

    override var source: SubstanceSource? = null
        protected set

    override var drain: SubstanceDrain? = null
        protected set

    override fun connectSource(source: SubstanceSource) {
        if (this.source == source) return
        this.source = source
        source.connectDrain(this)
    }

    override fun disconnectSource() {
        this.source?.let {
            this.source = null
            it.disconnectDrain()
        }
    }

    override fun connectDrain(drain: SubstanceDrain) {
        if (this.drain == drain) return
        this.drain = drain
        drain.connectSource(this)
    }

    override fun disconnectDrain() {
        this.drain?.let {
            this.drain = null
            it.disconnectSource()
        }
    }

    override fun push(substance: MutableSubstance, timeFrame: Measure<Time>, flowId: Long): Measure<Volume> {
        return drain?.let {
            if (checkId(flowId)) {
                it.push(substance, timeFrame, flowId)
            } else null
        } ?: (0.0 * Volume.liters)
    }

    override fun pull(amount: Measure<Volume>, timeFrame: Measure<Time>, flowId: Long): MutableSubstance {
        return source?.let {
            if (checkId(flowId)) {
                it.pull(amount, timeFrame, flowId)
            } else null
        } ?: MutableSubstance()
    }

    protected fun checkId(flowId: Long): Boolean {
        seenIDs.contains(flowId).let { seen ->
            if (seen) seenIDs.remove(flowId)
            else seenIDs.add(flowId)
            return !seen
        }
    }
}
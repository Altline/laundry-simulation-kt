package altline.appliance.electricity

import altline.appliance.electricity.transit.ElectricalDrain
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.electricity.transit.ElectricalSource
import altline.appliance.measure.Energy
import altline.appliance.measure.Power
import altline.appliance.measure.repeatPeriodically
import altline.appliance.util.CoroutineManager
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.random.Random

abstract class BasicElectricalDevice(
    final override val power: Measure<Power>
) : ElectricalDevice {

    private val inletDrain = object : ElectricalDrain {
        override val maxInputFlowRate = power
        override val realInputFlowRate = power
        override val inputs = arrayOf(ElectricalDrainPort(this))
        override val inputCount = inputs.size

        override fun pushFlow(
            flowable: MutableElectricalEnergy,
            timeFrame: Measure<Time>,
            flowId: Long
        ): Measure<Energy> {
            return flowable.amount
        }
    }

    final override val powerInlet = inletDrain.inputs[0]
    protected val powerSource: ElectricalSource?
        get() = powerInlet.connectedPort?.owner

    protected val tickPeriod = 0.5 * seconds

    private val coroutineManager = CoroutineManager(CoroutineScope(Dispatchers.Default)) {
        repeatPeriodically(tickPeriod) {
            operate()
        }
    }

    val running: Boolean
        get() = coroutineManager.active

    fun start() {
        coroutineManager.active = true
    }

    fun stop() {
        coroutineManager.active = false
    }

    protected open fun onStart() {}
    protected open fun onStop() {}

    protected abstract fun operate()

    protected fun pullEnergy(amount: Measure<Energy>, timeFrame: Measure<Time>): MutableElectricalEnergy? {
        return powerSource?.pullFlow(amount, timeFrame, Random.nextLong())
    }
}
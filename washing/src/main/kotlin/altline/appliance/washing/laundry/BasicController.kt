package altline.appliance.washing.laundry

import altline.appliance.electricity.BasicElectricalDevice
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.Power
import altline.appliance.util.logger
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.CoroutineScope

class BasicController(
    override val washCycles: List<LaundryWashCycle>,
    override val power: Measure<Power>
) : LaundryWasherController {
    private val log by logger()

    init {
        require(washCycles.isNotEmpty()) { "A controller needs to have at least one wash cycle program." }
    }

    private val electricalDevice = object : BasicElectricalDevice(power) {
        override fun operate() {
            val requiredEnergy = power * tickPeriod
            val availableEnergy = pullEnergy(requiredEnergy, tickPeriod)?.amount
            if (availableEnergy == null || availableEnergy < requiredEnergy) {
                powerOff()
            }
            if (cycleRunning) cycleRunningTime += tickPeriod
        }
    }

    override val powerInlet: ElectricalDrainPort
        get() = electricalDevice.powerInlet

    override var selectedWashCycle: LaundryWashCycle = washCycles.first()
        set(value) {
            if (value in washCycles) field = value
            else log.warn("The given wash cycle does not exist for the current washer ($value).")
        }

    override var activeWashCycle: LaundryWashCycle? = null
        private set

    override val poweredOn: Boolean
        get() = electricalDevice.running

    override val cycleRunning: Boolean
        get() = activeWashCycle?.running ?: false

    override val cyclePaused: Boolean
        get() = activeWashCycle?.paused ?: false

    override var cycleRunningTime: Measure<Time> = 0 * seconds
        private set

    override fun powerOn() {
        if (!poweredOn && powerInlet.isConnected) electricalDevice.start()
    }

    override fun powerOff() {
        if (poweredOn && !cycleRunning) electricalDevice.stop()
    }

    override fun toggleCyclePause() {
        activeWashCycle?.togglePause()
    }

    override fun startCycle(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        if (cycleRunning || !poweredOn) return
        cycleRunningTime = 0 * seconds
        activeWashCycle = selectedWashCycle
        selectedWashCycle.start(washer, coroutineScope)
    }

    override fun stopCycle() {
        if (!cycleRunning) return
        activeWashCycle?.stop()
        activeWashCycle = null
    }
}
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
                this@BasicController.stop()
            }
            runningTime += tickPeriod
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

    override val running: Boolean
        get() = electricalDevice.running

    override var runningTime: Measure<Time> = 0 * seconds
        private set

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        if (running) return
        runningTime = 0 * seconds
        electricalDevice.start()
        activeWashCycle = selectedWashCycle
        selectedWashCycle.start(washer, coroutineScope)
    }

    override fun stop() {
        if (!running) return
        activeWashCycle?.stop()
        activeWashCycle = null
        electricalDevice.stop()
    }

}
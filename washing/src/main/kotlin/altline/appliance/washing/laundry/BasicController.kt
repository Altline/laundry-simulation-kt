package altline.appliance.washing.laundry

import altline.appliance.electricity.BasicElectricalDevice
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.Power
import altline.appliance.util.logger
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import io.nacular.measured.units.*
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
        }
    }

    override val powerInlet: ElectricalDrainPort
        get() = electricalDevice.powerInlet

    override var running = false
        private set

    override var selectedWashCycle: LaundryWashCycle = washCycles.first()
        set(value) {
            if (value in washCycles) field = value
            else log.warn("The given wash cycle does not exist for the current washer ($value).")
        }
    private var activeWashCycle: LaundryWashCycle? = null

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        if (running) return
        running = true
        electricalDevice.start()
        activeWashCycle = selectedWashCycle
        selectedWashCycle.start(washer, coroutineScope)
    }

    override fun stop() {
        if (!running) return
        activeWashCycle?.stop()
        activeWashCycle = null
        electricalDevice.stop()
        running = false
    }

}
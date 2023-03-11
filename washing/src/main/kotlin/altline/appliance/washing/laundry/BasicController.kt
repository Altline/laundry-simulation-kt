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
            val requiredEnergy = power * timeFactor
            val availableEnergy = pullEnergy(requiredEnergy, timeFactor)?.amount
            if (availableEnergy == null || availableEnergy < requiredEnergy) {
                powerOff()
            }
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
        get() {
            if (field?.running == false) field = null
            return field
        }
        private set

    override val poweredOn: Boolean
        get() = electricalDevice.running

    override val cycleRunning: Boolean
        get() = activeWashCycle?.running ?: false

    override val cyclePaused: Boolean
        get() = activeWashCycle?.paused ?: false

    override val cycleRunningTime: Measure<Time>?
        get() = activeWashCycle?.runningTime

    override fun powerOn() {
        if (!poweredOn && powerInlet.isConnected) electricalDevice.start()
    }

    override fun powerOff() {
        if (poweredOn && !cycleRunning) electricalDevice.stop()
    }

    override fun toggleCyclePause() {
        if (!poweredOn) return
        activeWashCycle?.togglePause()
    }

    override fun startCycle(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        if (!poweredOn || cycleRunning) return
        activeWashCycle = selectedWashCycle
        selectedWashCycle.start(washer, coroutineScope)
    }

    override fun stopCycle() {
        if (!cycleRunning) return
        activeWashCycle?.stop()
        activeWashCycle = null
    }

    override fun increaseTemperature(): Boolean {
        if (!poweredOn || cycleRunning) return false

        with(selectedWashCycle) {
            if (selectedTemperatureSettingIndex == temperatureSettings.lastIndex)
                return false
            selectedTemperatureSettingIndex = selectedTemperatureSettingIndex?.plus(1)
        }
        return true
    }

    override fun decreaseTemperature(): Boolean {
        if (!poweredOn || cycleRunning) return false

        with(selectedWashCycle) {
            if (selectedTemperatureSettingIndex == 0)
                return false
            selectedTemperatureSettingIndex = selectedTemperatureSettingIndex?.minus(1)
        }
        return true
    }

    override fun increaseSpinSpeed(): Boolean {
        if (!poweredOn) return false

        with(selectedWashCycle) {
            if (selectedSpinSpeedSettingIndex == spinSpeedSettings.lastIndex)
                return false
            selectedSpinSpeedSettingIndex = selectedSpinSpeedSettingIndex?.plus(1)
        }
        return true
    }

    override fun decreaseSpinSpeed(): Boolean {
        if (!poweredOn) return false

        with(selectedWashCycle) {
            if (selectedSpinSpeedSettingIndex == 0)
                return false
            selectedSpinSpeedSettingIndex = selectedSpinSpeedSettingIndex?.minus(1)
        }
        return true
    }
}
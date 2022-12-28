package altline.appliance.washing.laundry

import altline.appliance.electricity.BasicElectricalDevice
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.Power
import altline.appliance.util.logger
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.times
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
        activeWashCycle?.togglePause()
    }

    override fun startCycle(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        if (cycleRunning || !poweredOn) return
        activeWashCycle = selectedWashCycle
        selectedWashCycle.start(washer, coroutineScope)
    }

    override fun stopCycle() {
        if (!cycleRunning) return
        activeWashCycle?.stop()
        activeWashCycle = null
    }

    override fun increaseTemperature() {
        if (cycleRunning) return

        with(selectedWashCycle) {
            if (selectedTemperatureSettingIndex == temperatureSettings.lastIndex)
                return
            selectedTemperatureSettingIndex = selectedTemperatureSettingIndex?.plus(1)
        }
    }

    override fun decreaseTemperature() {
        if (cycleRunning) return

        with(selectedWashCycle) {
            if (selectedTemperatureSettingIndex == 0)
                return
            selectedTemperatureSettingIndex = selectedTemperatureSettingIndex?.minus(1)
        }
    }

    override fun increaseSpinSpeed() {
        with(selectedWashCycle) {
            if (selectedSpinSpeedSettingIndex == spinSpeedSettings.lastIndex)
                return
            selectedSpinSpeedSettingIndex = selectedSpinSpeedSettingIndex?.plus(1)
        }
    }

    override fun decreaseSpinSpeed() {
        with(selectedWashCycle) {
            if (selectedSpinSpeedSettingIndex == 0)
                return
            selectedSpinSpeedSettingIndex = selectedSpinSpeedSettingIndex?.minus(1)
        }
    }
}
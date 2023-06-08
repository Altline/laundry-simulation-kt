package altline.appliance.washing.laundry

import altline.appliance.common.SpeedModifier
import altline.appliance.electricity.BasicElectricalDevice
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.*
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.spin.ElectricMotor
import altline.appliance.spin.SpinDirection
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.util.logger
import altline.appliance.washing.laundry.washCycle.CentrifugeParams
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class BasicController(
    override val washCycles: List<LaundryWashCycle>,
    override val power: Measure<Power>,
    private val config: LaundryWasherConfig
) : LaundryWasherController {

    private val log by logger()

    override var dispenser: LaundryWashDispenser? = null
    override var drum: Drum? = null
    override var drumMotor: ElectricMotor? = null
    override var pump: ElectricPump? = null
    override var thermostat: Thermostat? = null

    override var doorLocked: Boolean = false
        private set

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

    /**
     * A short-running job that operates after a cycle is paused of stopped to ensure the right conditions.
     */
    private var sideJob: Job? = null

    override fun powerOn() {
        if (!poweredOn && powerInlet.isConnected) electricalDevice.start()
    }

    override fun powerOff() {
        if (poweredOn && !cycleRunning && sideJob == null) electricalDevice.stop()
    }

    override fun startCycle(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        if (sideJob != null || !poweredOn || cycleRunning) return
        activeWashCycle = selectedWashCycle
        selectedWashCycle.start(washer, coroutineScope)
    }

    override fun stopCycle(coroutineScope: CoroutineScope) {
        if (sideJob != null) return

        stopAllActivities()

        if (!cycleRunning) return
        activeWashCycle?.stop()
        activeWashCycle = null

        sideJob = coroutineScope.launch {
            delay(1 * Time.seconds)
            drainUntilEmpty()
            delay(1 * Time.seconds)
            unlockDoor()
            sideJob = null
        }
    }

    override fun toggleCyclePause(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        if (sideJob != null || !poweredOn) return

        if (cycleRunning) {
            activeWashCycle?.togglePause()
            if (cyclePaused) {
                stopAllActivities()
                val drumLiquidAmount = drum?.excessLiquidAmount ?: (0 * liters)
                if (drumLiquidAmount <= config.doorSafeWaterLevel) {
                    sideJob = coroutineScope.launch {
                        delay(1 * Time.seconds)
                        unlockDoor()
                        sideJob = null
                    }
                }
            } else {
                lockDoor()
            }
        } else startCycle(washer, coroutineScope)
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

    private fun lockDoor() {
        doorLocked = true
    }

    private fun unlockDoor() {
        doorLocked = false
    }

    private suspend fun fillThroughDetergent(amount: Measure<Volume>) {
        dispenser?.dispenseMainDetergent()
        trackLiquidUntil { it >= amount }
        dispenser?.haltMainDetergent()
    }

    private suspend fun fillThroughSoftener(amount: Measure<Volume>) {
        dispenser?.dispenseMainSoftener()
        trackLiquidUntil { it >= amount }
        dispenser?.haltMainSoftener()
    }

    private suspend fun drainUntilEmpty() {
        val drumLiquidAmount = drum?.excessLiquidAmount ?: (0 * liters)
        if (drumLiquidAmount < 1 * liters) return
        startDrain()
        trackLiquidUntil { it.isNegligible() }
        stopDrain()
    }

    private fun startDrain() {
        pump?.start()
    }

    private fun stopDrain() {
        pump?.stop()
    }

    private suspend fun wash(params: WashParams) {
        with(params) {
            val setTemperature = temperature
            if (setTemperature != null) {
                thermostat?.triggerSetting = setTemperature
            }

            val cycleCount = (duration / (spinPeriod + restPeriod)).roundToInt()
            repeat(cycleCount) { i ->
                val direction = if (i % 2 == 0) SpinDirection.Positive else SpinDirection.Negative
                val currentTemperature = drum?.excessLiquid?.temperature
                if (setTemperature != null && currentTemperature != null) {
                    thermostat?.check(currentTemperature)
                } else if (drum?.heater?.running == true) {
                    drum?.heater?.stop()
                }

                spin(direction, spinSpeed, spinPeriod)
                delay(restPeriod / SpeedModifier)
            }

            if (drum?.heater?.running == true) drum?.heater?.stop()
        }
    }

    private suspend fun centrifuge(params: CentrifugeParams) {
        with(params) {
            startDrain()
            spin(SpinDirection.Positive, spinSpeed, duration)
            startDrain()
        }
    }

    private suspend fun spin(direction: SpinDirection, speed: Measure<Spin>, duration: Measure<Time>) {
        drumMotor?.speedSetting = speed
        drumMotor?.spinDirection = direction
        drumMotor?.start()
        delay(duration / SpeedModifier)
        drumMotor?.stop()
    }

    private fun stopAllActivities() {
        dispenser?.haltMainDetergent()
        dispenser?.haltMainSoftener()
        drum?.heater?.stop()
        drumMotor?.stop()
        pump?.stop()
    }

    private suspend fun trackLiquidUntil(
        measureRate: Measure<Frequency> = config.waterMeasureRate,
        condition: (liquidVolume: Measure<Volume>) -> Boolean
    ) {
        repeatPeriodically(measureRate) {
            drum?.let {
                if (condition(it.excessLiquidAmount)) return
            } ?: return
        }
    }
}
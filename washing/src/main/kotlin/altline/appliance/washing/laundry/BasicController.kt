package altline.appliance.washing.laundry

import altline.appliance.common.delaySpeedAware
import altline.appliance.electricity.BasicElectricalDevice
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.*
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.spin.ElectricMotor
import altline.appliance.spin.SpinDirection
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.util.logger
import altline.appliance.washing.laundry.washCycle.*
import altline.appliance.washing.laundry.washCycle.phase.*
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

open class BasicController(
    final override val washCycles: List<LaundryWashCycle>,
    final override val power: Measure<Power>,
    protected open val dispenser: LaundryWashDispenser,
    protected open val drum: Drum,
    protected open val drumMotor: ElectricMotor,
    protected open val pump: ElectricPump,
    protected open val thermostat: Thermostat,
    protected val config: LaundryWasherConfig
) : LaundryWasherController {

    init {
        require(washCycles.isNotEmpty()) { "A controller needs to have at least one wash cycle program." }
    }

    private val log by logger()

    private val electricalDevice = object : BasicElectricalDevice(power) {
        override fun operate() {
            val requiredEnergy = power * timeFactor
            val availableEnergy = pullEnergy(requiredEnergy, timeFactor)?.amount
            if (availableEnergy == null || availableEnergy < requiredEnergy) {
                stop()
            }
        }
    }

    final override val powerInlet: ElectricalDrainPort
        get() = electricalDevice.powerInlet

    final override val poweredOn: Boolean
        get() = electricalDevice.running

    final override var doorLocked: Boolean = false
        private set

    final override var selectedCycle: LaundryWashCycle = washCycles.first()
        set(value) {
            if (value in washCycles) {
                field = value
                selectedCycleStatus =
                    if (value == activeCycle) activeCycleStatus!!
                    else getCycleStatus(value)
            } else log.warn("The given wash cycle does not exist for the current washer ($value).")
        }

    final override var selectedCycleStatus: List<StageStatus> = getCycleStatus(selectedCycle)
        private set

    private var activeCycleStatus: List<StageStatus>? = null

    final override var activeCycle: LaundryWashCycle? = null
        private set

    final override val cycleRunning: Boolean
        get() = activeCycle != null

    final override var cyclePaused: Boolean = false
        private set

    final override val cycleRunningTime: Measure<Time>?
        get() =
            if (cycleRunning) selectedCycleStatus.sumOf { it.runningTime }
            else null

    final override val cycleRemainingTime: Measure<Time>?
        get() = activeCycle?.duration?.minus(cycleRunningTime ?: (0 * seconds))

    /** Queue of the remaining sections of the currently active wash cycle. */
    private val sectionQueue: Queue<SectionInstance> = LinkedList()

    /** The job of the currently active cycle. */
    private var cycleJob: Job? = null

    /** Indicates that an exclusive operation is running. Only one exclusive operation can be running at a time.
     * Some operations get ignored while an exclusive operation is running. */
    private var exclusiveOperationRunning = false

    private fun getCycleStatus(washCycle: LaundryWashCycle): List<StageStatus> {
        return washCycle.getStages().map { it.toStatusObject() }
    }

    override fun powerOn() {
        if (!poweredOn && powerInlet.isConnected) electricalDevice.start()
    }

    override fun powerOff() {
        if (poweredOn && !cycleRunning && !exclusiveOperationRunning) electricalDevice.stop()
    }

    override fun togglePreWash(): Boolean {
        if (!poweredOn || cycleRunning) return false
        val cycle = selectedCycle
        if (cycle !is PreWashCapable) return false
        cycle.preWash = !cycle.preWash
        selectedCycleStatus = getCycleStatus(selectedCycle)
        return true
    }

    override fun increaseTemperature(): Boolean {
        if (!poweredOn || cycleRunning) return false

        with(selectedCycle) {
            if (selectedTemperatureSettingIndex == temperatureSettings.lastIndex)
                return false
            selectedTemperatureSettingIndex = selectedTemperatureSettingIndex?.plus(1)
            selectedTemperatureSetting?.let { updateTemperature(it) }
        }
        return true
    }

    override fun decreaseTemperature(): Boolean {
        if (!poweredOn || cycleRunning) return false

        with(selectedCycle) {
            if (selectedTemperatureSettingIndex == 0)
                return false
            selectedTemperatureSettingIndex = selectedTemperatureSettingIndex?.minus(1)
            selectedTemperatureSetting?.let { updateTemperature(it) }
        }
        return true
    }

    private fun updateTemperature(temperature: Measure<Temperature>) {
        selectedCycleStatus.flatMap { stageStatus ->
            stageStatus.phases.map { it.phase }
                .filterIsInstance<WashPhase>()
        }.forEach { washPhase ->
            washPhase.sections
                .filter { it.params.temperature != null }
                .forEach { it.params = it.params.copy(temperature = temperature) }
        }
    }

    override fun increaseSpinSpeed(): Boolean {
        if (!poweredOn || (cycleRunning && selectedCycle != activeCycle)) return false

        with(selectedCycle) {
            if (selectedSpinSpeedSettingIndex == spinSpeedSettings.lastIndex)
                return false
            selectedSpinSpeedSettingIndex = selectedSpinSpeedSettingIndex?.plus(1)
            selectedSpinSpeedSetting?.let { updateCycleSpinSpeed(it) }
        }
        return true
    }

    override fun decreaseSpinSpeed(): Boolean {
        if (!poweredOn || (cycleRunning && selectedCycle != activeCycle)) return false

        with(selectedCycle) {
            if (selectedSpinSpeedSettingIndex == 0)
                return false
            selectedSpinSpeedSettingIndex = selectedSpinSpeedSettingIndex?.minus(1)
            selectedSpinSpeedSetting?.let { updateCycleSpinSpeed(it) }
        }
        return true
    }

    private fun updateCycleSpinSpeed(spinSpeed: Measure<Spin>) {
        selectedCycleStatus.flatMap { stageStatus ->
            stageStatus.phases.map { it.phase }
                .filterIsInstance<SpinPhase>()
        }.forEach { spinPhase ->
            spinPhase.sections.forEach { it.params = it.params.copy(spinSpeed = spinSpeed) }
        }
    }

    override fun startCycle(coroutineScope: CoroutineScope) {
        if (!poweredOn || (cycleRunning && !cyclePaused) || exclusiveOperationRunning) return

        activeCycle = selectedCycle
        activeCycleStatus = selectedCycleStatus

        if (sectionQueue.isEmpty()) {
            val sectionInstances = selectedCycleStatus.flatMap { stageStatus ->
                stageStatus.phases.flatMap { phaseStatus ->
                    phaseStatus.sections.filterIsInstance<SectionInstance>()
                }
            }
            sectionQueue.addAll(sectionInstances)
        }

        cycleJob = coroutineScope.launch {
            delay(0.5 * seconds)
            lockDoor()
            delay(0.5 * seconds)

            if (!cyclePaused) {
                drainUntilEmpty()
                delay(1 * seconds)
            } else cyclePaused = false

            while (sectionQueue.isNotEmpty()) {
                sectionQueue.element().run()
                sectionQueue.remove()
            }

            delay(1 * seconds)
            unlockDoor()

            activeCycle = null
            activeCycleStatus = null
            resetCycleStatus()
        }
    }

    override suspend fun stopCycle() {
        if (!cycleRunning) return
        doExclusiveOperation {
            cycleJob?.cancelAndJoin()
            stopAllActivities()
            delay(1 * seconds)
            drainUntilEmpty()
            delay(1 * seconds)
            unlockDoor()
            sectionQueue.clear()
            activeCycle = null
            activeCycleStatus = null
            cyclePaused = false
            resetCycleStatus()
        }
    }

    override suspend fun pauseCycle() {
        doExclusiveOperation {
            cycleJob?.cancelAndJoin()
            stopAllActivities()
            val drumLiquidAmount = drum.excessLiquidAmount
            if (drumLiquidAmount <= config.doorSafeWaterLevel) {
                delay(1 * seconds)
                unlockDoor()
            }
            cyclePaused = true
        }
    }

    private fun resetCycleStatus() {
        selectedCycle = selectedCycle
    }

    protected open suspend fun executeSection(sectionInstance: SectionStatus) {
        val section = sectionInstance.section
        val runningTime = sectionInstance.runningTime
        when (section) {
            is FillPhase.DetergentFillSection -> fillThroughDetergent(section.fillToAmount)
            is FillPhase.SoftenerFillSection -> fillThroughSoftener(section.fillToAmount)
            is DrainPhase.Section.FocusedDrain -> drainUntilEmpty()
            is DrainPhase.Section.TumbleDrain -> tumbleDrain(section.tumbleParams.withRemainingDuration(runningTime))
            is WashPhase.Section -> tumble(section.params.withRemainingDuration(runningTime))
            is SpinPhase.Section -> spin(section.params.withRemainingDuration(runningTime))
        }
        delaySpeedAware(section.endDelay)
    }

    private fun TumbleParams.withRemainingDuration(runningTime: Measure<Time>): TumbleParams {
        return copy(duration = duration - runningTime)
    }

    private fun SpinParams.withRemainingDuration(runningTime: Measure<Time>): SpinParams {
        return copy(duration = duration - runningTime)
    }

    private fun lockDoor() {
        doorLocked = true
    }

    private fun unlockDoor() {
        doorLocked = false
    }

    private suspend fun fillThroughDetergent(amount: Measure<Volume>) {
        dispenser.dispenseMainDetergent()
        trackLiquidUntil { it >= amount }
        dispenser.haltMainDetergent()
    }

    private suspend fun fillThroughSoftener(amount: Measure<Volume>) {
        dispenser.dispenseMainSoftener()
        trackLiquidUntil { it >= amount }
        dispenser.haltMainSoftener()
    }

    private suspend fun drainUntilEmpty() {
        val drumLiquidAmount = drum.excessLiquidAmount
        if (drumLiquidAmount < 1 * liters) return

        startDrain()
        trackLiquidUntil { it.isNegligible() }
        stopDrain()
    }

    private fun startDrain() {
        pump.start()
    }

    private fun stopDrain() {
        pump.stop()
    }

    private suspend fun tumble(params: TumbleParams) {
        val cycleCount = (params.duration / (params.spinPeriod + params.restPeriod)).roundToInt()
        repeat(cycleCount) { i ->
            val currentTemperature = drum.excessLiquid.temperature
            if (params.temperature != null && currentTemperature != null) {
                thermostat.triggerSetting = params.temperature
                thermostat.check(currentTemperature)
            } else if (drum.heater.running) {
                drum.heater.stop()
            }

            val direction = if (i % 2 == 0) SpinDirection.Positive else SpinDirection.Negative
            spin(direction, params.spinSpeed, params.spinPeriod)
            delaySpeedAware(params.restPeriod)
        }

        if (drum.heater.running) drum.heater.stop()
    }

    private suspend fun tumbleDrain(params: TumbleParams) {
        startDrain()
        tumble(params)
        stopDrain()
    }

    private suspend fun spin(params: SpinParams) {
        if (params.spinSpeed == 0 * rpm) return
        startDrain()
        drumMotor.speedSetting = params.spinSpeed
        drumMotor.spinDirection = SpinDirection.Positive
        drumMotor.start()

        repeatPeriodicallySpeedAware(1 * seconds, times = (params.duration `in` seconds).toInt()) {
            val selectedSpeed = activeCycle!!.selectedSpinSpeedSetting!!
            when {
                selectedSpeed == 0 * rpm -> {
                    drumMotor.stop()
                    stopDrain()
                    return
                }

                selectedSpeed != drumMotor.speedSetting -> drumMotor.speedSetting = selectedSpeed
            }
        }

        drumMotor.stop()
        stopDrain()
    }

    private suspend fun spin(direction: SpinDirection, speed: Measure<Spin>, duration: Measure<Time>) {
        drumMotor.speedSetting = speed
        drumMotor.spinDirection = direction
        drumMotor.start()
        delaySpeedAware(duration)
        drumMotor.stop()
    }

    protected open fun stopAllActivities() {
        dispenser.haltMainDetergent()
        dispenser.haltMainSoftener()
        drum.heater.stop()
        drumMotor.stop()
        pump.stop()
    }

    protected suspend fun trackLiquidUntil(
        measureRate: Measure<Frequency> = config.waterMeasureRate,
        condition: (liquidVolume: Measure<Volume>) -> Boolean
    ) {
        repeatPeriodicallySpeedAware(measureRate) {
            if (condition(drum.excessLiquidAmount)) return
        }
    }

    /**
     * Executes the given [operation] as an exclusive operation. Only one exclusive operation can be running at a time.
     * Some operations get ignored while an exclusive operation is running.
     */
    private suspend fun doExclusiveOperation(operation: suspend () -> Unit) {
        if (!exclusiveOperationRunning) {
            exclusiveOperationRunning = true
            operation()
            exclusiveOperationRunning = false
        }
    }

    private inner class SectionInstance(override val section: PhaseSection) : SectionStatus {
        private val timedExecutor = TimedExecutor()

        override val active: Boolean
            get() = timedExecutor.active

        override val runningTime: Measure<Time>
            get() = timedExecutor.runningTime

        suspend fun run() {
            timedExecutor.executeTimed { executeSection(this) }
        }
    }

    private fun CycleStage.toStatusObject(): StageStatus {
        return StageStatus(
            stage = this,
            phases = this.phases.map { phase ->
                PhaseStatus(
                    phase = phase,
                    sections = phase.sections.map { section ->
                        SectionInstance(section)
                    }
                )
            }
        )
    }
}

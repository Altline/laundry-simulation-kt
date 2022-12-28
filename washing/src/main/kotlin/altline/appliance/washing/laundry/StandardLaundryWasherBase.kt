package altline.appliance.washing.laundry

import altline.appliance.common.Body
import altline.appliance.common.RefreshPeriod
import altline.appliance.common.SpeedModifier
import altline.appliance.electricity.ElectricalDevice
import altline.appliance.electricity.transit.BasicElectricalConduit
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.*
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.BasicSubstanceConduit
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.substance.transit.SubstanceConduit
import altline.appliance.washing.Washer
import altline.appliance.washing.laundry.washCycle.CentrifugeParams
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import altline.appliance.washing.laundry.washCycle.WashParams
import altline.appliance.washing.laundry.washCycle.phase.SpinPhase
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.div
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

abstract class StandardLaundryWasherBase(
    protected open val controller: LaundryWasherController,
    protected open val dispenser: LaundryWashDispenser,
    protected open val drum: Drum,
    protected open val drumMotor: ElectricMotor,
    protected open val pump: ElectricPump,
    protected open val config: LaundryWasherConfig
) : Washer, ElectricalDevice {

    private val machineScope = CoroutineScope(Dispatchers.Default)

    override val power: Measure<Power>
        get() = TODO("Not yet implemented")

    private val powerInletConduit by lazy {
        BasicElectricalConduit(
            maxFlowRate = controller.power + drum.heater.power + drumMotor.power + pump.power,
            inputCount = 1,
            outputCount = 4
        ).apply {
            outputs[0] connectTo controller.powerInlet
            outputs[1] connectTo drum.heater.powerInlet
            outputs[2] connectTo drumMotor.powerInlet
            outputs[3] connectTo pump.powerInlet
        }
    }

    override val powerInlet: ElectricalDrainPort
        get() = powerInletConduit.inputs[0]

    open val fluidIntake: SubstanceConduit by lazy {
        BasicSubstanceConduit(config.intakeFlowRate)
    }

    open val fluidOutlet: SubstanceConduit by lazy {
        BasicSubstanceConduit(config.outputFlowRate)
    }

    val poweredOn: Boolean
        get() = controller.poweredOn

    override val running: Boolean
        get() = controller.cycleRunning

    val runningTime: Measure<Time>?
        get() = controller.cycleRunningTime

    override val load: Set<Body>
        get() = drum.load

    val washCycles: List<LaundryWashCycle>
        get() = controller.washCycles

    var selectedWashCycle: LaundryWashCycle
        get() = controller.selectedWashCycle
        set(value) {
            controller.selectedWashCycle = value
        }

    val activeWashCycle: LaundryWashCycle?
        get() = controller.activeWashCycle

    private var scanningJob: Job? = null
    var scanner: WasherStateScanner? = null
        set(value) {
            if (value != field) {
                field = value
                scanningJob?.cancel()
                if (value != null) {
                    scanningJob = machineScope.launch { scan() }
                }
            }
        }

    private suspend fun scan() {
        repeatPeriodically(RefreshPeriod) {
            scanner?.run {
                scanDrumMotor(drumMotor)
                scanWashLiquid(drum.excessLiquid)
            }
        }
    }

    override fun load(vararg items: Body) = drum.load(*items)
    override fun unload(vararg items: Body) = drum.unload(*items)
    override fun unloadAll(): List<Body> = drum.unloadAll()
    override fun start() = controller.startCycle(this, machineScope)
    override fun stop() = controller.stopCycle()

    fun togglePower() {
        if (controller.poweredOn) controller.powerOff()
        else controller.powerOn()
    }

    fun toggleCycleRun() {
        if (controller.cycleRunning) controller.toggleCyclePause()
        else start()
    }

    fun increaseTemperature() = controller.increaseTemperature()
    fun decreaseTemperature() = controller.decreaseTemperature()
    fun increaseSpinSpeed() = controller.increaseSpinSpeed().also { updateCentrifugeSpeed() }
    fun decreaseSpinSpeed() = controller.decreaseSpinSpeed().also { updateCentrifugeSpeed() }

    private fun updateCentrifugeSpeed() {
        if (activeWashCycle?.activePhase is SpinPhase) {
            drumMotor.speedSetting = activeWashCycle!!.selectedSpinSpeedSetting!!
        }
    }

    internal open suspend fun fillThroughDetergent(amount: Measure<Volume>) {
        dispenser.dispenseMainDetergent()
        trackLiquidUntil { it >= amount }
        dispenser.haltMainDetergent()
    }

    internal open suspend fun fillThroughSoftener(amount: Measure<Volume>) {
        dispenser.dispenseMainSoftener()
        trackLiquidUntil { it >= amount }
        dispenser.haltMainSoftener()
    }

    internal open suspend fun drainUntilEmpty() {
        startDrain()
        trackLiquidUntil { it.isNegligible() }
        stopDrain()
    }

    internal open suspend fun startDrain() {
        pump.start()
    }

    internal open suspend fun stopDrain() {
        pump.stop()
    }

    internal open suspend fun wash(params: WashParams) {
        with(params) {
            val cycleCount = (duration / (spinPeriod + restPeriod)).roundToInt()
            repeat(cycleCount) { i ->
                spin(spinSpeed, reverseDirection = i % 2 != 0, spinPeriod)
                delay(restPeriod / SpeedModifier)
            }
        }
    }

    internal open suspend fun centrifuge(params: CentrifugeParams) {
        with(params) {
            pump.start()
            spin(spinSpeed, reverseDirection = false, duration)
            pump.stop()
        }
    }

    private suspend fun spin(speed: Measure<Spin>, reverseDirection: Boolean, duration: Measure<Time>) {
        drumMotor.speedSetting = speed
        drumMotor.reverseDirection = reverseDirection
        drumMotor.start()
        delay(duration / SpeedModifier)
        drumMotor.stop()
    }

    protected suspend fun trackLiquidUntil(
        measureRate: Measure<Frequency> = config.waterMeasureRate,
        condition: (liquidVolume: Measure<Volume>) -> Boolean
    ) {
        repeatPeriodically(measureRate) {
            if (condition(drum.excessLiquidAmount)) return
        }
    }
}
package altline.appliance.washing.laundry

import altline.appliance.common.Body
import altline.appliance.common.RefreshPeriod
import altline.appliance.electricity.ElectricalDevice
import altline.appliance.electricity.transit.BasicElectricalConduit
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.Power
import altline.appliance.measure.repeatPeriodically
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.BasicSubstanceConduit
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.substance.transit.SubstanceConduit
import altline.appliance.washing.Washer
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import altline.appliance.washing.laundry.washCycle.StageStatus
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class StandardLaundryWasherBase internal constructor(
    protected open val controller: LaundryWasherController,
    protected open val dispenser: LaundryWashDispenser,
    protected open val drum: Drum,
    protected open val drumMotor: ElectricMotor,
    protected open val pump: ElectricPump,
    protected open val config: LaundryWasherConfig
) : Washer, ElectricalDevice {

    private val machineScope = CoroutineScope(Dispatchers.Default)

    override val power: Measure<Power>
        get() = controller.power + drum.heater.power + drumMotor.power + pump.power

    private val powerInletConduit by lazy {
        BasicElectricalConduit(
            maxFlowRate = power,
            inputCount = 1,
            outputCount = 4
        ).apply {
            outputs[0] connectTo controller.powerInlet
            outputs[1] connectTo drum.heater.powerInlet
            outputs[2] connectTo drumMotor.powerInlet
            outputs[3] connectTo pump.powerInlet
        }
    }

    final override val powerInlet: ElectricalDrainPort
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

    val paused: Boolean
        get() = controller.cyclePaused

    val runningTime: Measure<Time>?
        get() = controller.cycleRunningTime

    val remainingTime: Measure<Time>?
        get() = controller.cycleRemainingTime

    val washCycles: List<LaundryWashCycle>
        get() = controller.washCycles

    var selectedCycle: LaundryWashCycle
        get() = controller.selectedCycle
        set(value) {
            controller.selectedCycle = value
        }

    val selectedCycleStatus: List<StageStatus>
        get() = controller.selectedCycleStatus

    val activeCycle: LaundryWashCycle?
        get() = controller.activeCycle

    val doorLocked: Boolean
        get() = controller.doorLocked

    override val load: Set<Body>
        get() = drum.load

    private var scanningJob: Job? = null
    var scanner: WasherStateScanner? = null
        set(value) {
            if (value != field) {
                field = value
                scanningJob?.cancel()
                scanningJob =
                    if (value != null) machineScope.launch { startScan() }
                    else null
            }
        }

    private suspend fun startScan() {
        scanner?.washLiquid = drum.excessLiquid

        repeatPeriodically(RefreshPeriod) {
            scanner?.let { scanner ->
                baseScan(scanner)
                scanState(scanner)
            }
        }
    }

    private fun baseScan(scanner: WasherStateScanner) {
        with(scanner) {
            scanDispenser(dispenser)
            scanDrainPump(pump)
            scanDrumMotor(drumMotor)
        }
    }

    protected abstract fun scanState(scanner: WasherStateScanner)

    override fun load(vararg items: Body) {
        if (!doorLocked) drum.load(*items)
    }

    override fun unload(vararg items: Body) {
        if (!doorLocked) drum.unload(*items)
    }

    override fun unloadAll(): Set<Body> {
        return if (!doorLocked) drum.unloadAll() else emptySet()
    }

    override fun start() = controller.startCycle(machineScope)

    override fun stop() {
        machineScope.launch { controller.stopCycle() }
    }

    fun pause() {
        machineScope.launch { controller.pauseCycle() }
    }

    fun togglePower() = when {
        running -> stop()
        poweredOn -> controller.powerOff()
        else -> controller.powerOn()
    }

    fun toggleCycleRun() = when {
        paused -> start()
        running -> pause()
        else -> start()
    }

    fun togglePreWash() = controller.togglePreWash()
    fun increaseTemperature() = controller.increaseTemperature()
    fun decreaseTemperature() = controller.decreaseTemperature()
    fun increaseSpinSpeed() = controller.increaseSpinSpeed()
    fun decreaseSpinSpeed() = controller.decreaseSpinSpeed()
}
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
import altline.appliance.washing.laundry.washCycle.phase.SpinPhase
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    val paused: Boolean
        get() = controller.cyclePaused

    val runningTime: Measure<Time>?
        get() = controller.cycleRunningTime

    val doorLocked: Boolean
        get() = controller.doorLocked

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

    override fun unloadAll(): List<Body> {
        return if (!doorLocked) drum.unloadAll() else emptyList()
    }

    override fun start() = controller.startCycle(this, machineScope)
    override fun stop() = controller.stopCycle(machineScope)

    fun togglePower() = when {
        running -> stop()
        poweredOn -> controller.powerOff()
        else -> controller.powerOn()
    }

    fun toggleCycleRun() = controller.toggleCyclePause(this, machineScope)

    fun increaseTemperature() = controller.increaseTemperature()
    fun decreaseTemperature() = controller.decreaseTemperature()
    fun increaseSpinSpeed() = controller.increaseSpinSpeed().also { updateCentrifugeSpeed() }
    fun decreaseSpinSpeed() = controller.decreaseSpinSpeed().also { updateCentrifugeSpeed() }

    private fun updateCentrifugeSpeed() {
        if (activeWashCycle?.activePhase is SpinPhase) {
            drumMotor.speedSetting = activeWashCycle!!.selectedSpinSpeedSetting!!
        }
    }
}
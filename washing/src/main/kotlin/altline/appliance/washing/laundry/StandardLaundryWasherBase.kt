package altline.appliance.washing.laundry

import altline.appliance.common.Body
import altline.appliance.electricity.ElectricalDevice
import altline.appliance.electricity.transit.BasicElectricalConduit
import altline.appliance.electricity.transit.ElectricalDrainPort
import altline.appliance.measure.*
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.BasicSubstanceConduit
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.substance.transit.SubstanceConduit
import altline.appliance.washing.Washer
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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

    override val running: Boolean
        get() = controller.running

    val runningTime: Measure<Time>
        get() = controller.runningTime

    override val load: Set<Body>
        get() = drum.load

    val washCycles: List<LaundryWashCycle>
        get() = controller.washCycles

    var selectedWashCycle: LaundryWashCycle
        get() = controller.selectedWashCycle
        set(value) { controller.selectedWashCycle = value }

    override fun load(vararg items: Body) = drum.load(*items)
    override fun unload(vararg items: Body) = drum.unload(*items)
    override fun unloadAll(): List<Body> = drum.unloadAll()
    override fun start() = controller.start(this, machineScope)
    override fun stop() = controller.stop()

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

    internal open suspend fun drain() {
        pump.start()
        trackLiquidUntil { it.isNegligible() }
        pump.stop()
    }

    internal open suspend fun wash(params: WashParams) {
        with(params) {
            val cycleCount = (washDuration / (spinDuration + restDuration)).roundToInt()
            repeat(cycleCount) {
                spin(spinSpeed, spinDuration)
                delay(restDuration)
            }
        }
    }

    internal open suspend fun centrifuge(params: CentrifugeParams) {
        with(params) {
            pump.start()
            spin(spinSpeed, duration)
            pump.stop()
        }
    }

    private suspend fun spin(speed: Measure<Spin>, duration: Measure<Time>) {
        drumMotor.speedSetting = speed
        drumMotor.start()
        delay(duration)
        drumMotor.stop()
    }

    protected suspend fun trackLiquidUntil(
        measureRate: Measure<Frequency> = config.waterMeasureRate,
        condition: (liquidVolume: Measure<Volume>) -> Boolean
    ) {
        repeatPeriodically(measureRate) {
            if (condition(drum.excessLiquidAmount)) {
                return@repeatPeriodically
            }
        }
    }
}
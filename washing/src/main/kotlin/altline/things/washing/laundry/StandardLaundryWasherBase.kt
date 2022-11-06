package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.electricity.ElectricalDevice
import altline.things.electricity.transit.BasicElectricalConduit
import altline.things.electricity.transit.ElectricalDrainPort
import altline.things.measure.Power
import altline.things.measure.Volume
import altline.things.spin.ElectricMotor
import altline.things.substance.transit.BasicSubstanceConduit
import altline.things.substance.transit.ElectricPump
import altline.things.substance.transit.SubstanceConduit
import altline.things.washing.Washer
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

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

    override fun load(item: Body) = drum.load(item)
    override fun load(items: Collection<Body>) = drum.load(items)
    override fun unload(item: Body) = drum.unload(item)
    override fun unload(items: Collection<Body>) = drum.unload(items)
    override fun unload(): List<Body> = drum.unload()
    override fun start() = controller.start(this, machineScope)
    override fun stop() = controller.stop()

    internal abstract suspend fun fillThroughDetergent(amount: Measure<Volume>)
    internal abstract suspend fun fillThroughSoftener(amount: Measure<Volume>)
    internal abstract suspend fun drain()
    internal abstract suspend fun wash(params: WashParams)
    internal abstract suspend fun spin(params: CentrifugeParams)
}
package altline.appliance.data

import altline.appliance.common.Body
import altline.appliance.electricity.ElectricHeater
import altline.appliance.electricity.transit.InfiniteElectricalSource
import altline.appliance.fabric.Clothing
import altline.appliance.fabric.Shirt
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.measure.kilowatts
import altline.appliance.measure.watts
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.CommonSubstanceTypes
import altline.appliance.substance.MutableSubstance
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.substance.transit.Reservoir
import altline.appliance.washing.laundry.*
import altline.appliance.washing.laundry.washCycle.CottonCycle
import altline.appliance.washing.laundry.washCycle.RinseCycle
import altline.appliance.washing.laundry.washCycle.SpinCycle
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class World {

    val washer: StandardLaundryWasherBase
    val laundry: Set<Body>

    init {
        laundry = setOf(
            Shirt(Clothing.Size.S, 80 * milliliters).apply {
                soak(MutableSubstance(CommonSubstanceTypes.WATER, 30 * milliliters, 20 * celsius))
                stain(MutableSubstance(CommonSubstanceTypes.WATER, 200 * milliliters, 20 * celsius))
            },
            Shirt(Clothing.Size.M, 100 * milliliters).apply {
                soak(MutableSubstance(CommonSubstanceTypes.WATER, 80 * milliliters, 20 * celsius))
                stain(MutableSubstance(CommonSubstanceTypes.WATER, 20 * milliliters, 20 * celsius))
            },
            Shirt(Clothing.Size.L, 120 * milliliters).apply {
                soak(MutableSubstance(CommonSubstanceTypes.WATER, 30 * milliliters, 20 * celsius))
                stain(MutableSubstance(CommonSubstanceTypes.WATER, 10 * milliliters, 20 * celsius))
            },
        )

        val powerSource = InfiniteElectricalSource(10 * kilowatts)

        val waterSource = Reservoir(
            capacity = 1000000 * liters,
            outflowThreshold = 0 * liters,
            shouldSpontaneouslyPush = true,
            inputFlowRate = 0 * liters / seconds,
            outputFlowRate = 100 * liters / seconds,
            initialSubstance = MutableSubstance(CommonSubstanceTypes.WATER, 1000000 * liters, 20 * celsius)
        )
        val fluidDrain = Reservoir(
            capacity = 1000000 * liters,
            outflowThreshold = 0 * liters,
            shouldSpontaneouslyPush = false,
            inputFlowRate = 100 * liters / seconds,
            outputFlowRate = 0 * liters / seconds
        )

        val config = LaundryWasherConfig()
        val controller = BasicController(
            washCycles = listOf(
                CottonCycle(),
                RinseCycle(),
                SpinCycle()
            ),
            power = 5 * watts
        )
        val dispenser = PreWashSlottedDispenser(
            preWashDetergentCapacity = 100 * milliliters,
            mainDetergentCapacity = 300 * milliliters,
            mainSoftenerCapacity = 100 * milliliters,
            config = config
        )
        val heater = ElectricHeater(
            power = 1700 * watts
        )
        val drum = BasicDrum(
            capacity = 100 * liters,
            heater = heater,
            config
        )
        val drumMotor = ElectricMotor(
            power = 400 * watts,
            maxSpeed = 1000 * rpm
        )
        val pump = ElectricPump(
            power = 40 * watts,
            maxFlowRate = config.outputFlowRate,
            inputCount = 1,
            outputCount = 1
        )

        drumMotor.connectedLoad = drum

        dispenser.outputPort connectTo drum.inputs[0]
        drum.outputs[0] connectTo pump.substanceInputs[0]

        washer = HouseholdLaundryWasher(controller, dispenser, drum, drumMotor, pump, config)
        washer.fluidIntake.outputs[0] connectTo dispenser.inputPort
        pump.substanceOutputs[0] connectTo washer.fluidOutlet.inputs[0]

        waterSource.outputs[0] connectTo washer.fluidIntake.inputs[0]
        washer.fluidOutlet.outputs[0] connectTo fluidDrain.inputs[0]

        powerSource.outputs[0] connectTo washer.powerInlet

        washer.load(laundry.first())
    }
}
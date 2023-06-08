package altline.appliance.washing.laundry

import altline.appliance.electricity.ElectricHeater
import altline.appliance.measure.Spin
import altline.appliance.measure.Volume
import altline.appliance.measure.watts
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.washing.laundry.washCycle.CottonCycle
import altline.appliance.washing.laundry.washCycle.RinseCycle
import altline.appliance.washing.laundry.washCycle.SpinCycle
import io.nacular.measured.units.*

class HouseholdLaundryWasherFactory {

    fun createHouseholdLaundryWasher(): HouseholdLaundryWasher {
        val config = LaundryWasherConfig()
        val controller = BasicController(
            washCycles = listOf(
                CottonCycle(),
                RinseCycle(),
                SpinCycle()
            ),
            power = 5 * watts,
            config = config
        )
        val dispenser = PreWashSlottedDispenser(
            preWashDetergentCapacity = 150 * Volume.milliliters,
            mainDetergentCapacity = 300 * Volume.milliliters,
            mainSoftenerCapacity = 100 * Volume.milliliters,
            config = config
        )
        val heater = ElectricHeater(
            power = 1700 * watts
        )
        val drum = BasicDrum(
            capacity = 100 * Volume.liters,
            heater = heater,
            config
        )
        val drumMotor = ElectricMotor(
            power = 400 * watts,
            maxSpeed = 1600 * Spin.rpm
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

        val washer = HouseholdLaundryWasher(controller, dispenser, drum, drumMotor, pump, config)
        washer.fluidIntake.outputs[0] connectTo dispenser.inputPort
        pump.substanceOutputs[0] connectTo washer.fluidOutlet.inputs[0]

        return washer
    }
}
package altline.appliance.washing.laundry

import altline.appliance.electricity.ElectricHeater
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Thermostat
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.Volume.Companion.milliliters
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
        val dispenser = PreWashSlottedDispenser(
            preWashDetergentCapacity = 150 * milliliters,
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
            maxSpeed = 1600 * rpm
        )
        val pump = ElectricPump(
            power = 40 * watts,
            maxFlowRate = config.outputFlowRate,
            inputCount = 1,
            outputCount = 1
        )
        val thermostat = Thermostat(
            triggerSetting = 20 * celsius,
            tolerance = 5 * celsius,
            onDropBelow = { drum.heater.start() },
            onRiseAbove = { drum.heater.stop() }
        )
        val controller = HouseholdLaundryWasherController(
            washCycles = listOf(
                CottonCycle(),
                RinseCycle(),
                SpinCycle()
            ),
            power = 5 * watts,
            dispenser = dispenser,
            drum = drum,
            drumMotor = drumMotor,
            pump = pump,
            thermostat = thermostat,
            config = config
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
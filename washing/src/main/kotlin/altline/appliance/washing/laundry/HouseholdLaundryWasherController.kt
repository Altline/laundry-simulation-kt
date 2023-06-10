package altline.appliance.washing.laundry

import altline.appliance.measure.Power
import altline.appliance.measure.Thermostat
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import io.nacular.measured.units.*

class HouseholdLaundryWasherController(
    washCycles: List<LaundryWashCycle>,
    power: Measure<Power>,
    override val dispenser: PreWashSlottedDispenser,
    override val drum: BasicDrum,
    override val drumMotor: ElectricMotor,
    override val pump: ElectricPump,
    override val thermostat: Thermostat,
    config: LaundryWasherConfig
) : BasicController(washCycles, power, dispenser, drum, drumMotor, pump, thermostat, config) {

    override fun stopAllActivities() {
        super.stopAllActivities()
        dispenser.haltPreWashDetergent()
    }
}
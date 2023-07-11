package altline.appliance.washing.laundry

import altline.appliance.measure.Power
import altline.appliance.measure.Thermostat
import altline.appliance.measure.Volume
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import altline.appliance.washing.laundry.washCycle.SectionStatus
import altline.appliance.washing.laundry.washCycle.phase.FillPhase
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

    override suspend fun executeSection(sectionInstance: SectionStatus) {
        super.executeSection(sectionInstance)
        when (val section = sectionInstance.section) {
            is FillPhase.PreWashFillSection -> fillThroughPreWash(section.fillToAmount)
        }
    }

    private suspend fun fillThroughPreWash(amount: Measure<Volume>) {
        dispenser.dispensePreWashDetergent()
        trackLiquidUntil { it >= amount }
        dispenser.haltPreWashDetergent()
    }

    override fun stopAllActivities() {
        super.stopAllActivities()
        dispenser.haltPreWashDetergent()
    }
}
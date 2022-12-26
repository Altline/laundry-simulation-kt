package altline.appliance.washing.laundry

import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.ElectricPump

open class HouseholdLaundryWasher(
    override val controller: LaundryWasherController,
    override val dispenser: PreWashSlottedDispenser,
    override val drum: BasicDrum,
    override val drumMotor: ElectricMotor,
    override val pump: ElectricPump,
    override val config: LaundryWasherConfig
) : StandardLaundryWasherBase(controller, dispenser, drum, drumMotor, pump, config) {

}
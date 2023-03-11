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

    val dispenserTray: PreWashSlottedDispenser.Tray
        get() = dispenser.tray

    override fun scanState(scanner: WasherStateScanner) {
        if (scanner !is HouseholdLaundryWasherScanner) return
        scanner.scanPreWashDispenser(dispenser)
    }

    override fun stopAllActivities() {
        super.stopAllActivities()
        dispenser.haltPreWashDetergent()
    }

    fun openDispenserTray(): PreWashSlottedDispenser.Tray {
        dispenser.openTray()
        return dispenserTray
    }

    fun closeDispenserTray() = dispenser.closeTray()
}
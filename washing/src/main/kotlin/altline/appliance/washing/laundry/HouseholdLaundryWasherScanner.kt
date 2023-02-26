package altline.appliance.washing.laundry

/**
 * An extension of [WasherStateScanner] designed to support [HouseholdLaundryWasher]s.
 */
class HouseholdLaundryWasherScanner : WasherStateScanner() {

    var fillingPreWash: Boolean = false
        private set

    internal fun scanPreWashDispenser(dispenser: PreWashSlottedDispenser) {
        fillingPreWash = dispenser.isDispensingPreWashDetergent
    }
}
package altline.appliance.washing.laundry

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.spin.ElectricMotor
import altline.appliance.spin.SpinDirection
import altline.appliance.substance.Substance
import altline.appliance.substance.transit.ElectricPump
import io.nacular.measured.units.*

/**
 * A tool designed for scanning the internal state of [StandardLaundryWasherBase] objects. A washer that had its state
 * scanned into this scanner will have it exposed here for reading. This base class has only the state values relevant
 * to [StandardLaundryWasherBase] and not its subclasses. The extending classes of this scanner can add additional
 * values to support different full implementations of standard laundry washers.
 */
open class WasherStateScanner {

    var fillingDetergent: Boolean = false
        private set

    var fillingSoftener: Boolean = false
        private set

    var draining: Boolean = false
        private set

    var spinSpeed: Measure<Spin> = 0 * rpm
        private set

    var spinDirection: SpinDirection = SpinDirection.Positive
        private set

    var washLiquid: Substance? = null
        internal set

    internal fun scanDispenser(dispenser: LaundryWashDispenser) {
        fillingDetergent = dispenser.isDispensingMainDetergent
        fillingSoftener = dispenser.isDispensingMainSoftener
    }

    internal fun scanDrainPump(drainPump: ElectricPump) {
        draining = drainPump.running
    }

    internal fun scanDrumMotor(drumMotor: ElectricMotor) {
        spinSpeed = drumMotor.currentSpeed
        spinDirection = drumMotor.spinDirection
    }
}
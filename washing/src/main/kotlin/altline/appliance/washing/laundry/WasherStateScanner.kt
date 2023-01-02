package altline.appliance.washing.laundry

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.Substance
import io.nacular.measured.units.Measure
import io.nacular.measured.units.times

class WasherStateScanner {

    var spinSpeed: Measure<Spin> = 0 * rpm
        private set

    var reverseSpinDirection: Boolean = false
        private set

    var washLiquid: Substance? = null

    fun scanDrumMotor(drumMotor: ElectricMotor) {
        spinSpeed = drumMotor.currentSpeed
        reverseSpinDirection = drumMotor.reverseDirection
    }
}
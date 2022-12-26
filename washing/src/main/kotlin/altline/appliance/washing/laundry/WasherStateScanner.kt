package altline.appliance.washing.laundry

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Volume
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.Substance
import io.nacular.measured.units.*

class WasherStateScanner {

    var spinSpeed: Measure<Spin> = 0 * rpm
        private set

    var reverseSpinDirection: Boolean = false
        private set

    var waterLevel: Measure<Volume> = 0 * liters
        private set

    fun scanDrumMotor(drumMotor: ElectricMotor) {
        spinSpeed = drumMotor.currentSpeed
        reverseSpinDirection = drumMotor.reverseDirection
    }

    fun scanWashLiquid(washLiquid: Substance) {
        waterLevel = washLiquid.amount
    }
}
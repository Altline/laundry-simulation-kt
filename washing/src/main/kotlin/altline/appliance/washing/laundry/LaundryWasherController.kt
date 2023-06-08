package altline.appliance.washing.laundry

import altline.appliance.electricity.ElectricalDevice
import altline.appliance.measure.Thermostat
import altline.appliance.spin.ElectricMotor
import altline.appliance.substance.transit.ElectricPump
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope

interface LaundryWasherController: ElectricalDevice {
    var dispenser: LaundryWashDispenser?
    var drum: Drum?
    var drumMotor: ElectricMotor?
    var pump: ElectricPump?
    var thermostat: Thermostat?

    val washCycles: List<LaundryWashCycle>
    var selectedWashCycle: LaundryWashCycle
    val activeWashCycle: LaundryWashCycle?

    val poweredOn: Boolean
    val doorLocked: Boolean
    val cycleRunning: Boolean
    val cyclePaused: Boolean

    val cycleRunningTime: Measure<Time>?

    fun powerOn()
    fun powerOff()

    fun startCycle(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stopCycle(coroutineScope: CoroutineScope)
    fun toggleCyclePause(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)

    fun increaseTemperature(): Boolean
    fun decreaseTemperature(): Boolean
    fun increaseSpinSpeed(): Boolean
    fun decreaseSpinSpeed(): Boolean
}
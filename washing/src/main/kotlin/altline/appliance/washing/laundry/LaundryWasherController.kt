package altline.appliance.washing.laundry

import altline.appliance.electricity.ElectricalDevice
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope

interface LaundryWasherController: ElectricalDevice {
    val washCycles: List<LaundryWashCycle>
    var selectedWashCycle: LaundryWashCycle
    val activeWashCycle: LaundryWashCycle?

    val poweredOn: Boolean
    val cycleRunning: Boolean
    val cyclePaused: Boolean

    val cycleRunningTime: Measure<Time>

    fun togglePower()
    fun toggleCyclePause()
    fun startCycle(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stopCycle()
}
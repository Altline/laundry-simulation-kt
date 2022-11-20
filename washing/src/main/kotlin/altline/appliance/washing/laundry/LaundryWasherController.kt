package altline.appliance.washing.laundry

import altline.appliance.electricity.ElectricalDevice
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import kotlinx.coroutines.CoroutineScope

interface LaundryWasherController: ElectricalDevice {
    val washCycles: List<LaundryWashCycle>
    var selectedWashCycle: LaundryWashCycle
    val running: Boolean

    fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stop()
}
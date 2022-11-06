package altline.things.washing.laundry

import altline.things.electricity.ElectricalDevice
import altline.things.washing.laundry.washCycle.LaundryWashCycle
import kotlinx.coroutines.CoroutineScope

interface LaundryWasherController: ElectricalDevice {
    val washCycles: List<LaundryWashCycle>
    var selectedWashCycle: LaundryWashCycle
    val running: Boolean

    fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stop()
}
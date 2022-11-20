package altline.appliance.washing.laundry.washCycle

import altline.appliance.washing.laundry.StandardLaundryWasherBase
import kotlinx.coroutines.CoroutineScope

interface LaundryWashCycle {
    val name: String
    fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stop()
}
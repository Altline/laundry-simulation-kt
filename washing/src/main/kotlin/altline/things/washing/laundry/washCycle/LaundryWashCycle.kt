package altline.things.washing.laundry.washCycle

import altline.things.washing.laundry.StandardLaundryWasherBase
import kotlinx.coroutines.CoroutineScope

interface LaundryWashCycle {
    val name: String
    fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stop()
}
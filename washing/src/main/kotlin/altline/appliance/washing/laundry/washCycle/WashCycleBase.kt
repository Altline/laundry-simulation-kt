package altline.appliance.washing.laundry.washCycle

import altline.appliance.washing.laundry.StandardLaundryWasherBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class WashCycleBase(
    override val stages: List<CycleStage>
) : LaundryWashCycle {

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            ensureStartingConditions(washer)
            stages.forEach { it.execute(washer) }
        }
    }

    private suspend fun ensureStartingConditions(washer: StandardLaundryWasherBase) {
        TODO("Not yet implemented")
    }
}
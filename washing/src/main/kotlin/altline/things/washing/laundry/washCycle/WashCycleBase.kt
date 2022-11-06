package altline.things.washing.laundry.washCycle

import altline.things.washing.laundry.StandardLaundryWasherBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class WashCycleBase : LaundryWashCycle {

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            ensureStartingConditions()
            executeProgram(washer)
        }
    }

    private suspend fun ensureStartingConditions() {

    }

    abstract suspend fun executeProgram(washer: StandardLaundryWasherBase)
}
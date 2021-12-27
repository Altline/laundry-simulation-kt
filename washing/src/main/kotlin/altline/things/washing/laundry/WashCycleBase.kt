package altline.things.washing.laundry

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class WashCycleBase(
    override val machine: LaundryWasher
) : LaundryWasher.WashCycle {

    override fun start(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            ensureStartingConditions()
            executeProgram()
        }
    }

    private suspend fun ensureStartingConditions() {

    }

    abstract suspend fun executeProgram()
}
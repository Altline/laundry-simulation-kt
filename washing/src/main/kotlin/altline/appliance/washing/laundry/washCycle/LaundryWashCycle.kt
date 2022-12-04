package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope

interface LaundryWashCycle {
    val stages: List<CycleStage>

    val estimatedDuration: Measure<Time>
        get() = stages.sumOf { it.estimatedDuration }

    fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stop()
}
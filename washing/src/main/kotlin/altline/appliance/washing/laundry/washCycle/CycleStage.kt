package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Volume
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.phase.*
import io.nacular.measured.units.*

@WashCycleDsl
class CycleStage {
    var phases: List<CyclePhase> = emptyList()
        private set

    val activePhase: CyclePhase?
        get() = phases.find { it.active }

    val estimatedDuration: Measure<Time>
        get() = phases.sumOf { it.duration }

    val runningTime: Measure<Time>
        get() = phases.sumOf { it.runningTime }

    private var resumingPhase: CyclePhase? = null

    suspend fun execute(washer: StandardLaundryWasherBase) {
        phases.forEach {
            if (resumingPhase == null || it == resumingPhase) {
                it.execute(washer)
                resumingPhase = null
            }
        }
    }

    fun onPause() {
        resumingPhase = activePhase
    }

    fun reset() {
        resumingPhase = null
        phases.forEach { it.reset() }
    }

    fun detergentFillPhase(fillToAmount: Measure<Volume>): DetergentFillPhase {
        return DetergentFillPhase(fillToAmount).also {
            phases += it
        }
    }

    fun softenerFillPhase(fillToAmount: Measure<Volume>): SoftenerFillPhase {
        return SoftenerFillPhase(fillToAmount).also {
            phases += it
        }
    }

    fun washPhase(init: WashPhase.() -> Unit): WashPhase {
        return WashPhase().also {
            it.init()
            phases += it
        }
    }

    fun drainPhase(
        duration: Measure<Time>,
        spinPeriod: Measure<Time>,
        restPeriod: Measure<Time>,
        spinSpeed: Measure<Spin>
    ): DrainPhase {
        return DrainPhase(
            WashParams(duration, spinPeriod, restPeriod, spinSpeed)
        ).also {
            phases += it
        }
    }

    fun spinPhase(
        duration: Measure<Time>,
        spinSpeed: Measure<Spin>
    ): SpinPhase {
        return SpinPhase(
            CentrifugeParams(duration, spinSpeed)
        ).also {
            phases += it
        }
    }
}
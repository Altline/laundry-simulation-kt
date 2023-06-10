package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.measure.Volume
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.washCycle.phase.*
import io.nacular.measured.units.*

@WashCycleDsl
class CycleStage {
    var phases: List<CyclePhase> = emptyList()
        private set

    val duration: Measure<Time>
        get() = phases.sumOf { it.duration }

    fun setTemperature(temperature: Measure<Temperature>?) {
        phases.filterIsInstance<WashPhase>()
            .forEach { it.setTemperature(temperature) }
    }

    fun setSpinSpeed(spinSpeed: Measure<Spin>?) {
        phases.filterIsInstance<SpinPhase>()
            .forEach { it.setSpinSpeed(spinSpeed) }
    }

    fun fillPhase(init: FillPhase.() -> Unit): FillPhase {
        return FillPhase().also {
            it.init()
            phases += it
        }
    }

    fun detergentFillPhase(fillToAmount: Measure<Volume>): FillPhase {
        return fillPhase {
            detergentFillSection(fillToAmount)
        }
    }

    fun softenerFillPhase(fillToAmount: Measure<Volume>): FillPhase {
        return fillPhase {
            softenerFillSection(fillToAmount)
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

    fun spinPhase(init: SpinPhase.() -> Unit): SpinPhase {
        return SpinPhase().also {
            it.init()
            phases += it
        }
    }

    fun spinPhase(
        duration: Measure<Time>,
        spinSpeed: Measure<Spin>,
        endDelay: Measure<Time>
    ): SpinPhase {
        return spinPhase {
            section(duration, spinSpeed, endDelay)
        }
    }
}
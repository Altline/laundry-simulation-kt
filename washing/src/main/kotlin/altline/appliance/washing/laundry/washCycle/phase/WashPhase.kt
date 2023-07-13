package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.washCycle.TumbleParams
import altline.appliance.washing.laundry.washCycle.WashCycleDsl
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

@WashCycleDsl
class WashPhase : CyclePhase {

    override var sections: List<Section> = emptyList()
        private set

    override val duration: Measure<Time>
        get() = sections.sumOf { it.duration }

    fun section(
        duration: Measure<Time>,
        spinPeriod: Measure<Time>,
        restPeriod: Measure<Time>,
        spinSpeed: Measure<Spin>,
        temperature: Measure<Temperature>? = null
    ): Section {
        return Section(
            params = TumbleParams(duration, spinPeriod, restPeriod, spinSpeed, temperature),
            adjustableTemperature = temperature == null
        ).also {
            sections += it
        }
    }

    data class Section(
        val params: TumbleParams,
        val adjustableTemperature: Boolean
    ) : PhaseSection {
        override val endDelay = 1 * seconds

        override val duration: Measure<Time>
            get() = params.duration + endDelay
    }
}
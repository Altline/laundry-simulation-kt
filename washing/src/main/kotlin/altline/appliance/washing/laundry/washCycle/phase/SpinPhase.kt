package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.washCycle.CentrifugeParams
import altline.appliance.washing.laundry.washCycle.WashCycleDsl
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

@WashCycleDsl
class SpinPhase : CyclePhase {

    override var sections: List<Section> = emptyList()
        private set

    override val duration: Measure<Time>
        get() = if (disabled) 0 * seconds
        else sections.sumOf { it.duration }

    var disabled: Boolean = false
        private set

    fun setSpinSpeed(spinSpeed: Measure<Spin>?) {
        disabled = spinSpeed == 0 * rpm
        sections.forEach { it.setSpinSpeed(spinSpeed) }
    }

    fun section(
        duration: Measure<Time>,
        spinSpeed: Measure<Spin>? = null,
        endDelay: Measure<Time> = 0 * seconds
    ): Section {
        return Section(
            params = CentrifugeParams(duration, spinSpeed ?: (0 * rpm)),
            adjustableSpeed = spinSpeed == null,
            endDelay = endDelay
        ).also {
            sections += it
        }
    }

    data class Section(
        val params: CentrifugeParams,
        val adjustableSpeed: Boolean,
        override val endDelay: Measure<Time>
    ) : PhaseSection {
        override val duration: Measure<Time>
            get() = if (params.spinSpeed != 0 * rpm) params.duration + endDelay
            else 0 * seconds

        fun setSpinSpeed(spinSpeed: Measure<Spin>?) {
            if (adjustableSpeed) params.spinSpeed = spinSpeed ?: (0 * rpm)
        }
    }
}
package altline.appliance.washing.laundry.washCycle.phase

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.measure.delay
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.WashParams
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

class WashPhase : CyclePhaseBase() {

    private val endDelay = 1 * seconds

    var sections: List<Section> = emptyList()
        private set

    override val duration: Measure<Time>
        get() = sections.sumOf { it.duration } + endDelay

    override suspend fun doExecute(washer: StandardLaundryWasherBase) {
        sections.forEach { it.execute(washer) }
        delay(endDelay)
    }

    fun section(
        duration: Measure<Time>,
        spinPeriod: Measure<Time>,
        restPeriod: Measure<Time>,
        spinSpeed: Measure<Spin>,
        temperature: Measure<Temperature>? = null
    ): Section {
        return Section(
            WashParams(duration, spinPeriod, restPeriod, spinSpeed, temperature)
        ).also {
            sections += it
        }
    }

    class Section(
        val washParams: WashParams
    ) {
        val duration: Measure<Time>
            get() = washParams.duration

        private val timedWrapper = TimedWrapper()

        val runningTime: Measure<Time>
            get() = timedWrapper.runningTime

        val active: Boolean
            get() = timedWrapper.active

        suspend fun execute(washer: StandardLaundryWasherBase) {
            timedWrapper.executeTimed { washer.wash(washParams) }
        }
    }
}
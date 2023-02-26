package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.measure.delay
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.phase.CyclePhase
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import org.koitharu.pausingcoroutinedispatcher.PausingJob
import org.koitharu.pausingcoroutinedispatcher.launchPausing

@DslMarker
annotation class WashCycleDsl

@WashCycleDsl
abstract class WashCycleBase : LaundryWashCycle {

    final override var stages: List<CycleStage> = emptyList()
        private set

    override val activeStage: CycleStage?
        get() = stages.find { it.activePhase != null }

    override val activePhase: CyclePhase?
        get() = activeStage?.activePhase

    override val selectedTemperatureSetting: Measure<Temperature>?
        get() = selectedTemperatureSettingIndex?.let { temperatureSettings[it] }

    override var selectedTemperatureSettingIndex: Int? = null
        set(value) {
            require(value in temperatureSettings.indices || value == null)
            field = value
            onTemperatureChanged(selectedTemperatureSetting)
        }

    override val selectedSpinSpeedSetting: Measure<Spin>?
        get() = selectedSpinSpeedSettingIndex?.let { spinSpeedSettings[it] }

    override var selectedSpinSpeedSettingIndex: Int? = null
        set(value) {
            require(value in spinSpeedSettings.indices || value == null)
            field = value
            onSpinSpeedChanged(selectedSpinSpeedSetting)
        }

    private var job: PausingJob? = null

    final override val running: Boolean
        get() = job?.isActive ?: false

    final override val paused: Boolean
        get() = job?.isPaused ?: false

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        job = coroutineScope.launchPausing {
            with(washer) {
                delay(0.5 * seconds)
                lockDoor()
                delay(0.5 * seconds)
                drainUntilEmpty()
                delay(2 * seconds)
                stages.forEach { it.execute(washer) }
                unlockDoor()
            }
        }
    }

    override fun stop() {
        job?.cancel()
    }

    override fun togglePause() {
        if (paused) job?.resume()
        else job?.pause()
    }

    protected fun addStage(init: CycleStage.() -> Unit): CycleStage {
        return CycleStage().also {
            it.init()
            stages += it
        }
    }

    protected abstract fun onTemperatureChanged(value: Measure<Temperature>?)
    protected abstract fun onSpinSpeedChanged(value: Measure<Spin>?)
}
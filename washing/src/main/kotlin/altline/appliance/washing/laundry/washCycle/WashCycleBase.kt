package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import org.koitharu.pausingcoroutinedispatcher.PausingJob
import org.koitharu.pausingcoroutinedispatcher.launchPausing

@DslMarker
annotation class WashCycleDsl

@WashCycleDsl
abstract class WashCycleBase : LaundryWashCycle {

    final override var stages: List<CycleStage> = emptyList()
        private set

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
            ensureStartingConditions(washer)
            stages.forEach { it.execute(washer) }
        }
    }

    override fun stop() {
        job?.cancel()
    }

    override fun togglePause() {
        if (paused) job?.resume()
        else job?.pause()
    }

    private suspend fun ensureStartingConditions(washer: StandardLaundryWasherBase) {
        washer.drain()
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
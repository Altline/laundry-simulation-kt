package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.measure.delay
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.phase.CyclePhase
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

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

    private var job: Job? = null
    private var washer: StandardLaundryWasherBase? = null
    private var coroutineScope: CoroutineScope? = null
    private var resumingStage: CycleStage? = null

    final override val running: Boolean
        get() = paused || (job?.isActive ?: false)

    final override var paused: Boolean = false
        private set

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        this.washer = washer
        this.coroutineScope = coroutineScope
        job = coroutineScope.launch {
            if (resumingStage == null) {
                delay(0.5 * seconds)
                washer.lockDoor()
                delay(0.5 * seconds)
                washer.drainUntilEmpty()
                delay(2 * seconds)
            }

            stages.forEach {
                if (resumingStage == null || it == resumingStage) {
                    it.execute(washer)
                    resumingStage = null
                }
            }

            delay(2 * seconds)
            washer.unlockDoor()

            reset()
        }
    }

    override fun stop() {
        reset()
    }

    override fun togglePause() {
        if (paused) resume()
        else pause()
    }

    private fun pause() {
        // Only enable pausing when we have entered a stage
        activeStage?.let { activeStage ->
            paused = true
            activeStage.onPause()
            resumingStage = activeStage
            job?.cancel()
            job = null
        }
    }

    private fun resume() {
        paused = false
        start(washer!!, coroutineScope!!)
    }

    private fun reset() {
        paused = false
        washer = null
        coroutineScope = null
        resumingStage = null
        stages.forEach { it.reset() }
        job?.cancel()
        job = null
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
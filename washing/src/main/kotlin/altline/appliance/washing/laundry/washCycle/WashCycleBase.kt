package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            ensureStartingConditions(washer)
            stages.forEach { it.execute(washer) }
        }
    }

    private suspend fun ensureStartingConditions(washer: StandardLaundryWasherBase) {
        TODO("Not yet implemented")
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
package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import io.nacular.measured.units.*

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
            stages.forEach { it.setTemperature(selectedTemperatureSetting) }
        }

    override val selectedSpinSpeedSetting: Measure<Spin>?
        get() = selectedSpinSpeedSettingIndex?.let { spinSpeedSettings[it] }

    override var selectedSpinSpeedSettingIndex: Int? = null
        set(value) {
            require(value in spinSpeedSettings.indices || value == null)
            field = value
            stages.forEach { it.setSpinSpeed(selectedSpinSpeedSetting) }
        }

    protected fun addStage(init: CycleStage.() -> Unit): CycleStage {
        return CycleStage().also {
            it.init()
            stages += it
        }
    }
}
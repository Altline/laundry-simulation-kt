package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import io.nacular.measured.units.*

@DslMarker
annotation class WashCycleDsl

abstract class WashCycleBase : LaundryWashCycle {

    override val selectedTemperatureSetting: Measure<Temperature>?
        get() = selectedTemperatureSettingIndex?.let { temperatureSettings[it] }

    override var selectedTemperatureSettingIndex: Int? = null
        set(value) {
            require(value in temperatureSettings.indices || value == null)
            field = value
        }

    override val selectedSpinSpeedSetting: Measure<Spin>?
        get() = selectedSpinSpeedSettingIndex?.let { spinSpeedSettings[it] }

    override var selectedSpinSpeedSettingIndex: Int? = null
        set(value) {
            require(value in spinSpeedSettings.indices || value == null)
            field = value
        }

    protected fun buildCycle(recipe: StageBuilder.() -> Unit): List<CycleStage> {
        return StageBuilder().apply(recipe).get()
    }

    @WashCycleDsl
    protected class StageBuilder {
        private val stages = mutableListOf<CycleStage>()

        fun stage(init: CycleStage.() -> Unit): CycleStage {
            return CycleStage().also {
                it.init()
                stages += it
            }
        }

        fun get(): List<CycleStage> {
            return stages.toList()
        }
    }
}
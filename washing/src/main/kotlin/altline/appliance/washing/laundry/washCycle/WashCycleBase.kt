package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class WashCycleBase(
    override val stages: List<CycleStage>
) : LaundryWashCycle {

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

    override fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            ensureStartingConditions(washer)
            stages.forEach { it.execute(washer) }
        }
    }

    private suspend fun ensureStartingConditions(washer: StandardLaundryWasherBase) {
        TODO("Not yet implemented")
    }
}
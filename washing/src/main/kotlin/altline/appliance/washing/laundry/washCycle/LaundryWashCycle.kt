package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.measure.sumOf
import io.nacular.measured.units.*

interface LaundryWashCycle {
    val stages: List<CycleStage>

    val duration: Measure<Time>
        get() = stages.sumOf { it.duration }

    val temperatureSettings: List<Measure<Temperature>>
    val selectedTemperatureSetting: Measure<Temperature>?
    var selectedTemperatureSettingIndex: Int?

    val spinSpeedSettings: List<Measure<Spin>>
    val selectedSpinSpeedSetting: Measure<Spin>?
    var selectedSpinSpeedSettingIndex: Int?
}
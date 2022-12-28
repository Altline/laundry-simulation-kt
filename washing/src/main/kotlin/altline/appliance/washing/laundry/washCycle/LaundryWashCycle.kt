package altline.appliance.washing.laundry.washCycle

import altline.appliance.measure.Spin
import altline.appliance.measure.Temperature
import altline.appliance.measure.sumOf
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.phase.CyclePhase
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import kotlinx.coroutines.CoroutineScope

interface LaundryWashCycle {
    val stages: List<CycleStage>
    val activeStage: CycleStage?
    val activePhase: CyclePhase?

    val temperatureSettings: List<Measure<Temperature>>
    val selectedTemperatureSetting: Measure<Temperature>?
    var selectedTemperatureSettingIndex: Int?

    val spinSpeedSettings: List<Measure<Spin>>
    val selectedSpinSpeedSetting: Measure<Spin>?
    var selectedSpinSpeedSettingIndex: Int?

    val running: Boolean
    val paused: Boolean

    val estimatedDuration: Measure<Time>
        get() = stages.sumOf { it.estimatedDuration }

    val runningTime: Measure<Time>
        get() = stages.sumOf { it.runningTime }

    fun start(washer: StandardLaundryWasherBase, coroutineScope: CoroutineScope)
    fun stop()
    fun togglePause()
}
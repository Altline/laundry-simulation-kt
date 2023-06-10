package altline.appliance.washing.laundry

import altline.appliance.electricity.ElectricalDevice
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import altline.appliance.washing.laundry.washCycle.StageStatus
import io.nacular.measured.units.*
import kotlinx.coroutines.CoroutineScope

interface LaundryWasherController: ElectricalDevice {
    val poweredOn: Boolean
    val doorLocked: Boolean

    val washCycles: List<LaundryWashCycle>
    var selectedCycle: LaundryWashCycle
    val selectedCycleStatus: List<StageStatus>
    val activeCycle: LaundryWashCycle?
    val cycleRunning: Boolean
    val cyclePaused: Boolean
    val cycleRunningTime: Measure<Time>?
    val cycleRemainingTime: Measure<Time>?

    fun powerOn()
    fun powerOff()

    fun increaseTemperature(): Boolean
    fun decreaseTemperature(): Boolean
    fun increaseSpinSpeed(): Boolean
    fun decreaseSpinSpeed(): Boolean

    fun startCycle(coroutineScope: CoroutineScope)
    suspend fun stopCycle()
    suspend fun pauseCycle()
}
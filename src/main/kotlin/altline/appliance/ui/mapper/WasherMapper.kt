package altline.appliance.ui.mapper

import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.ui.component.washer.ControlPanelUi
import altline.appliance.ui.component.washer.DispenserPanelUi
import altline.appliance.ui.component.washer.StatusPanelUi
import altline.appliance.ui.component.washer.WasherPanelUi
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.util.clockFormat
import altline.appliance.ui.util.optionalDecimal
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.CottonCycle
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import altline.appliance.washing.laundry.washCycle.RinseCycle
import altline.appliance.washing.laundry.washCycle.SpinCycle
import kotlin.math.roundToInt

class WasherMapper {

    fun mapToWasherPanel(
        washer: StandardLaundryWasherBase,
        onDispenserClick: () -> Unit,
        onSelectNextCycle: (reverse: Boolean) -> Unit,
        onPowerOnOff: () -> Unit,
        onStartPause: () -> Unit,
        onTempUp: () -> Unit,
        onTempDown: () -> Unit,
        onRpmUp: () -> Unit,
        onRpmDown: () -> Unit
    ): WasherPanelUi {
        val shownWashCycle = washer.activeWashCycle ?: washer.selectedWashCycle
        return WasherPanelUi(
            dispenserPanelUi = DispenserPanelUi(
                onClick = onDispenserClick
            ),
            controlPanelUi = ControlPanelUi(
                washCycles = washer.washCycles.map(this::mapToWashCycleName),
                selectedCycle = mapToWashCycleName(washer.selectedWashCycle),
                onSelectNextCycle = onSelectNextCycle,
                onPowerOnOff = onPowerOnOff,
                onStartPause = onStartPause,
                onTempUp = onTempUp,
                onTempDown = onTempDown,
                onRpmUp = onRpmUp,
                onRpmDown = onRpmDown
            ),
            statusPanelUi = if (washer.poweredOn) {
                StatusPanelUi(
                    timer = shownWashCycle.estimatedDuration.clockFormat(showSeconds = false),
                    temperature = shownWashCycle.selectedTemperatureSetting?.optionalDecimal() ?: "-",
                    spinSpeed = "${shownWashCycle.selectedSpinSpeedSetting?.`in`(rpm)?.roundToInt() ?: "-"}"
                )
            } else null
        )
    }

    fun mapToWashCycleName(washCycle: LaundryWashCycle): String {
        return when (washCycle) {
            is CottonCycle -> strings["washCycle_cotton"]
            is RinseCycle -> strings["washCycle_rinse"]
            is SpinCycle -> strings["washCycle_spin"]
            else -> ""
        }
    }
}
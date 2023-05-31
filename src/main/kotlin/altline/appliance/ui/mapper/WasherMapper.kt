package altline.appliance.ui.mapper

import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Volume
import altline.appliance.spin.SpinDirection
import altline.appliance.substance.SubstanceType
import altline.appliance.ui.component.washer.*
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.util.clockFormat
import altline.appliance.ui.util.optionalDecimal
import altline.appliance.washing.laundry.PreWashSlottedDispenser
import altline.appliance.washing.laundry.SimpleSlottedDispenser
import altline.appliance.washing.laundry.SlottedDispenser
import altline.appliance.washing.laundry.StandardLaundryWasherBase
import altline.appliance.washing.laundry.washCycle.CottonCycle
import altline.appliance.washing.laundry.washCycle.LaundryWashCycle
import altline.appliance.washing.laundry.washCycle.RinseCycle
import altline.appliance.washing.laundry.washCycle.SpinCycle
import io.nacular.measured.units.*
import kotlin.math.roundToInt

class WasherMapper(
    private val colorMapper: ColorMapper
) {
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
            } else null,
            drumUi = DrumUi(
                spinSpeed = washer.scanner?.spinSpeed ?: (0 * rpm),
                spinDirection = washer.scanner?.spinDirection ?: SpinDirection.Positive
            )
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

    fun mapDispenserTray(
        tray: SlottedDispenser.Tray,
        selectedAdditive: SubstanceType,
        onAdditiveAdd: (SlottedDispenser.Tray.Slot, Measure<Volume>) -> Unit,
        onAdditiveRemove: (SlottedDispenser.Tray.Slot, Measure<Volume>) -> Unit,
        onAdditivePick: (SubstanceType) -> Unit,
        onCloseTray: () -> Unit
    ): DispenserTrayUi {
        return when (tray) {
            is SimpleSlottedDispenser.Tray -> DispenserTrayUi(
                preWashSlot = null,
                mainSlot = mapDispenserSlot(
                    tray.mainDetergentSlot,
                    onAdditiveAdd = onAdditiveAdd,
                    onAdditiveRemove = onAdditiveRemove
                ),
                softenerSlot = mapDispenserSlot(
                    tray.mainSoftenerSlot,
                    onAdditiveAdd = onAdditiveAdd,
                    onAdditiveRemove = onAdditiveRemove
                ),
                selectedAdditive = selectedAdditive,
                onAdditivePick = onAdditivePick,
                onCloseTray = onCloseTray
            )

            is PreWashSlottedDispenser.Tray -> DispenserTrayUi(
                preWashSlot = mapDispenserSlot(
                    tray.preWashDetergentSlot,
                    onAdditiveAdd = onAdditiveAdd,
                    onAdditiveRemove = onAdditiveRemove
                ),
                mainSlot = mapDispenserSlot(
                    tray.mainDetergentSlot,
                    onAdditiveAdd = onAdditiveAdd,
                    onAdditiveRemove = onAdditiveRemove
                ),
                softenerSlot = mapDispenserSlot(
                    tray.mainSoftenerSlot,
                    onAdditiveAdd = onAdditiveAdd,
                    onAdditiveRemove = onAdditiveRemove
                ),
                selectedAdditive = selectedAdditive,
                onAdditivePick = onAdditivePick,
                onCloseTray = onCloseTray
            )

            else -> throw IllegalArgumentException("Unsupported dispenser tray type.")
        }
    }

    private fun mapDispenserSlot(
        slot: SlottedDispenser.Tray.Slot,
        onAdditiveAdd: (SlottedDispenser.Tray.Slot, Measure<Volume>) -> Unit,
        onAdditiveRemove: (SlottedDispenser.Tray.Slot, Measure<Volume>) -> Unit
    ): DispenserSlotUi {
        return DispenserSlotUi(
            capacity = slot.capacity,
            additiveAmount = slot.additive.amount,
            additiveColor = colorMapper.mapSubstanceToColor(slot.additive),
            onAdditiveAdd = { onAdditiveAdd(slot, it) },
            onAdditiveRemove = { onAdditiveRemove(slot, it) }
        )
    }
}
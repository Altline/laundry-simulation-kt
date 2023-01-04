package altline.appliance.ui.component.washer

import altline.appliance.measure.Volume
import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.measure.divSameUnit
import altline.appliance.substance.CommonFabricSofteners
import altline.appliance.substance.SubstanceType
import altline.appliance.ui.mapper.ColorMapper
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.SubstanceColors
import altline.appliance.ui.util.modifiedColor
import altline.appliance.ui.util.optionalDecimal
import altline.appliance.washing.CommonDetergents
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.nacular.measured.units.Measure
import io.nacular.measured.units.times
import org.koin.core.context.GlobalContext

@Composable
fun DispenserTray(data: DispenserTrayUi) = with(data) {
    Row(
        Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        if (preWashSlot != null) Slot(preWashSlot, 250.dp)
        Slot(mainSlot, 250.dp)
        Slot(softenerSlot, 120.dp)

        Spacer(Modifier.width(12.dp))
        AdditivePicker(data.selectedAdditive, data.onAdditivePick)
    }
}

@Composable
private fun Slot(data: DispenserSlotUi, height: Dp) {
    val width = ((data.capacity `in` milliliters) / height.value * 100).dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { data.onAdditiveAdd(0.1 * data.capacity) }) {
            Icon(
                Icons.Default.Add,
                contentDescription = strings["dispenser_addAdditive_contentDesc"]
            )
        }

        val shape = RoundedCornerShape(8.dp)
        Box(
            Modifier
                .size(width, height)
                .clip(shape)
                .background(MaterialTheme.colors.surface)
                .border(1.dp, Color.LightGray, shape),
            contentAlignment = Alignment.BottomCenter
        ) {
            val fillHeight = (height.value * data.additiveAmount.divSameUnit(data.capacity)).dp
            Box(
                Modifier
                    .fillMaxWidth()
                    .requiredHeight(fillHeight)
                    .background(data.additiveColor ?: MaterialTheme.colors.background)
                    .border(1.dp, Color.LightGray)
            )
        }

        IconButton(onClick = { data.onAdditiveRemove(0.1 * data.capacity) }) {
            Icon(
                Icons.Default.Remove,
                contentDescription = strings["dispenser_removeAdditive_contentDesc"]
            )
        }

        Text(
            text = data.additiveAmount.optionalDecimal(mandatoryUnits = milliliters),
            color = modifiedColor(alpha = ContentAlpha.medium),
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun AdditivePicker(
    selectedAdditive: SubstanceType,
    onAdditivePick: (SubstanceType) -> Unit
) {
    val tileSize = 50.dp
    val powerStrings = listOf(
        strings["power_ultimate"],
        strings["power_strong"],
        strings["power_basic"],
        strings["power_mild"],
        strings["power_weak"],
        strings["power_barely"],
        strings["power_useless"],
    )

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(
            Modifier.width(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = strings["power_title"],
                Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.body2,
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.End
            )
            for (power in powerStrings) {
                Text(
                    text = power,
                    Modifier
                        .fillMaxWidth()
                        .height(tileSize)
                        .wrapContentHeight(Alignment.CenterVertically),
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.End
                )
            }
        }
        AdditiveTileColumn(
            title = strings["substanceType_detergents"],
            additives = CommonDetergents.values(),
            selectedAdditive = selectedAdditive,
            tileSize = tileSize,
            onAdditivePick = onAdditivePick
        )
        AdditiveTileColumn(
            title = strings["substanceType_softeners"],
            additives = CommonFabricSofteners.values(),
            selectedAdditive = selectedAdditive,
            tileSize = tileSize,
            onAdditivePick = onAdditivePick
        )
    }
}

@Composable
private fun <T : SubstanceType> AdditiveTileColumn(
    title: String,
    additives: Array<T>,
    selectedAdditive: SubstanceType,
    tileSize: Dp,
    onAdditivePick: (SubstanceType) -> Unit
) {
    val colorMapper = GlobalContext.get().get<ColorMapper>()

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body2
        )
        for (additive in additives) {
            AdditiveTile(
                size = tileSize,
                color = colorMapper.mapSubstanceTypeToColor(additive),
                selected = additive == selectedAdditive,
                onClick = { onAdditivePick(additive) }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AdditiveTile(
    size: Dp,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    val border =
        if (selected) BorderStroke(3.dp, Color.LightGray)
        else BorderStroke(1.dp, Color.LightGray)

    val elevation = if (selected) 8.dp else 0.dp

    Surface(
        selected = selected,
        onClick = onClick,
        Modifier.size(size),
        shape = RoundedCornerShape(8.dp),
        color = color,
        border = border,
        elevation = elevation
    ) {}
}

data class DispenserTrayUi(
    val preWashSlot: DispenserSlotUi?,
    val mainSlot: DispenserSlotUi,
    val softenerSlot: DispenserSlotUi,
    val selectedAdditive: SubstanceType,
    val onAdditivePick: (SubstanceType) -> Unit,
    val onCloseTray: () -> Unit
) {
    companion object {
        @Composable
        fun preview() = DispenserTrayUi(
            preWashSlot = DispenserSlotUi.preview(),
            mainSlot = DispenserSlotUi.preview(),
            softenerSlot = DispenserSlotUi.preview(),
            selectedAdditive = CommonDetergents.BASIC_DETERGENT,
            onAdditivePick = {},
            onCloseTray = {}
        )
    }
}

data class DispenserSlotUi(
    val capacity: Measure<Volume>,
    val additiveAmount: Measure<Volume>,
    val additiveColor: Color?,
    val onAdditiveAdd: (Measure<Volume>) -> Unit,
    val onAdditiveRemove: (Measure<Volume>) -> Unit
) {
    companion object {
        @Composable
        fun preview() = DispenserSlotUi(
            capacity = 100 * milliliters,
            additiveAmount = 40 * milliliters,
            additiveColor = SubstanceColors.BasicDetergent,
            onAdditiveAdd = {},
            onAdditiveRemove = {}
        )
    }
}
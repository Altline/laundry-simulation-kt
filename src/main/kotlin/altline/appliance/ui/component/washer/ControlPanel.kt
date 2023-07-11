package altline.appliance.ui.component.washer

import altline.appliance.ui.resources.AppIcons
import altline.appliance.ui.resources.appicons.PlayPause
import altline.appliance.ui.resources.appicons.PreWash
import altline.appliance.ui.resources.appicons.Spin
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.util.modifiedColor
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.PointerButton
import androidx.compose.ui.unit.dp

@Composable
fun ControlPanel(
    data: ControlPanelUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        with(data) {
            LeftButtons(
                onPowerOnOff, onStartPause
            )
            CycleSelector(
                washCycles, selectedCycle, onSelectNextCycle
            )
            RightButtons(
                onPreWashClick, onTempUp, onTempDown, onRpmUp, onRpmDown
            )
        }

    }
}

@Composable
private fun LeftButtons(
    onPowerOnOff: () -> Unit,
    onStartPause: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ControlButton(
            icon = Icons.Default.PowerSettingsNew,
            contentDescription = strings["controlPanel_power_contentDesc"],
            onClick = onPowerOnOff,
            Modifier.size(16.dp, 60.dp),
        )
        ControlButton(
            icon = AppIcons.PlayPause,
            contentDescription = strings["controlPanel_start_contentDesc"],
            onClick = onStartPause,
            Modifier.size(16.dp, 60.dp),
        )
    }
}

@Composable
private fun RightButtons(
    onPreWashClick: () -> Unit,
    onTempUp: () -> Unit,
    onTempDown: () -> Unit,
    onRpmUp: () -> Unit,
    onRpmDown: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        ControlButton(
            icon = AppIcons.PreWash,
            contentDescription = strings["controlPanel_preWash_contentDesc"],
            onClick = onPreWashClick,
            modifier = Modifier.size(40.dp, 16.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.height(80.dp).offset(y = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ControlButton(
                    icon = Icons.Default.ArrowDropUp,
                    contentDescription = strings["controlPanel_increase_contentDesc"],
                    onClick = onTempUp,
                    Modifier.size(16.dp, 24.dp),
                )
                Spacer(Modifier.height(12.dp))
                ControlButton(
                    icon = Icons.Default.ArrowDropDown,
                    contentDescription = strings["controlPanel_decrease_contentDesc"],
                    onClick = onTempDown,
                    Modifier.size(16.dp, 24.dp),
                )
                Icon(
                    Icons.Default.DeviceThermostat,
                    contentDescription = strings["temperature_contentDesc"],
                    tint = modifiedColor(alpha = ContentAlpha.medium)
                )
            }

            Column(
                Modifier.height(80.dp).offset(y = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ControlButton(
                    icon = Icons.Default.ArrowDropUp,
                    contentDescription = strings["controlPanel_increase_contentDesc"],
                    onClick = onRpmUp,
                    Modifier.size(16.dp, 24.dp),
                )
                Spacer(Modifier.height(12.dp))
                ControlButton(
                    icon = Icons.Default.ArrowDropDown,
                    contentDescription = strings["controlPanel_decrease_contentDesc"],
                    onClick = onRpmDown,
                    Modifier.size(16.dp, 24.dp),
                )
                Icon(
                    AppIcons.Spin,
                    contentDescription = strings["spinSpeed_contentDesc"],
                    tint = modifiedColor(alpha = ContentAlpha.medium)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CycleSelector(
    cycles: List<String>,
    selectedCycle: String,
    onSelectNextCycle: (reverse: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier) {
        val currentIndex = cycles.indexOf(selectedCycle)
        var prevIndex by remember { mutableStateOf(currentIndex) }
        val delta = 360f / cycles.size
        var angle by remember { mutableStateOf(currentIndex * delta) }

        // Rotation logic that supports wrapping around the starting angle without rotating in reverse
        if (prevIndex == cycles.lastIndex && currentIndex == 0) {
            angle += delta
        } else if (prevIndex == 0 && currentIndex == cycles.lastIndex) {
            angle -= delta
        } else if (currentIndex > prevIndex) {
            angle += delta
        } else if (currentIndex < prevIndex) {
            angle -= delta
        }

        prevIndex = currentIndex

        val animatedAngle by animateFloatAsState(angle)

        Surface(
            Modifier
                .align(Alignment.Center)
                .size(60.dp)
                .clip(CircleShape)
                .graphicsLayer { rotationZ = animatedAngle }
                .onClick(
                    matcher = PointerMatcher.mouse(PointerButton.Primary)
                ) { onSelectNextCycle(false) }
                .onClick(
                    matcher = PointerMatcher.mouse(PointerButton.Secondary)
                ) { onSelectNextCycle(true) }
                .indication(interactionSource, LocalIndication.current)
                .hoverable(interactionSource),
            color = Color.LightGray
        ) {
            Box(
                Modifier
                    .requiredSize(4.dp, 20.dp)
                    .offset(y = -24.dp)
                    .clip(RoundedCornerShape(bottomStartPercent = 100, bottomEndPercent = 100))
                    .background(MaterialTheme.colors.primaryVariant)
            )
        }

        cycles.forEachIndexed { index, _ ->
            val tickAngle = index * (360f / cycles.size)
            val iconModifier = Modifier
                .size(5.dp)
                .align(Alignment.Center)
                .rotate(tickAngle)
                .offset(y = -38.dp)

            Icon(
                Icons.Filled.Circle,
                contentDescription = null,
                iconModifier,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
private fun ControlButton(
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ControlButton(
        icon = rememberVectorPainter(icon),
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun ControlButton(
    icon: Painter,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick,
        modifier,
        contentPadding = PaddingValues(0.dp)
    ) {
        Icon(icon, contentDescription)
    }
}

data class ControlPanelUi(
    val washCycles: List<String>,
    val selectedCycle: String,
    val onSelectNextCycle: (reverse: Boolean) -> Unit,
    val onPowerOnOff: () -> Unit,
    val onStartPause: () -> Unit,
    val onPreWashClick: () -> Unit,
    val onTempUp: () -> Unit,
    val onTempDown: () -> Unit,
    val onRpmUp: () -> Unit,
    val onRpmDown: () -> Unit
) {
    companion object {
        @Composable
        fun preview() = ControlPanelUi(
            washCycles = listOf(
                "Cotton", "Mix", "Rinse", "Spin"
            ),
            selectedCycle = "Mix",
            onSelectNextCycle = {},
            onPowerOnOff = {},
            onStartPause = {},
            onPreWashClick = {},
            onTempUp = {},
            onTempDown = {},
            onRpmUp = {},
            onRpmDown = {}
        )
    }
}

@Preview
@Composable
private fun PreviewControlPanel() {
    AppTheme {
        ControlPanel(ControlPanelUi.preview())
    }
}
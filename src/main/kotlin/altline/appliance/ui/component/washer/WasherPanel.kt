package altline.appliance.ui.component.washer

import altline.appliance.ui.component.VerticalDivider
import altline.appliance.ui.theme.AppTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WasherPanel(
    data: WasherPanelUi,
    modifier: Modifier = Modifier
) {
    // Reference size of the panel. The subcomponents can safely assume that this is always the actual size
    // because the whole thing is scaled in post
    val refWidth = 600.dp
    val refHeight = 800.dp

    BoxWithConstraints(modifier.aspectRatio(refWidth / refHeight)) {
        Column(
            Modifier
                .align(Alignment.Center)
                .requiredSize(refWidth, refHeight)
                .scale(minOf(maxWidth / refWidth, maxHeight / refHeight))
        ) {
            val headWeight = 0.2f
            Divider(thickness = 3.dp)
            WasherHead(data, Modifier.weight(headWeight).fillMaxWidth())
            Divider()
            WasherBody(data, Modifier.weight(1f - headWeight).fillMaxWidth())
            Divider(thickness = 3.dp)
        }
    }
}

@Composable
private fun WasherHead(
    data: WasherPanelUi,
    modifier: Modifier = Modifier
) {
    Row(modifier.background(Color.White)) {
        DispenserPanel(
            data.dispenserPanelUi,
            Modifier.weight(1f).fillMaxHeight()
        )
        VerticalDivider()
        ControlPanel(
            data.controlPanelUi,
            Modifier.weight(1f).fillMaxHeight()
        )
        VerticalDivider()
        StatusPanel(
            data.statusPanelUi,
            Modifier.weight(1f).fillMaxHeight()
        )
    }
}

@Composable
private fun WasherBody(
    data: WasherPanelUi,
    modifier: Modifier = Modifier
) {
    Box(
        modifier.background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Drum(data.drumUi)
        FilterDoor()
    }
}

@Composable
private fun BoxScope.FilterDoor() {
    Surface(
        Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 20.dp, bottom = 20.dp)
            .size(150.dp, 100.dp),
        shape = RoundedCornerShape(10),
        border = BorderStroke(1.dp, Color(0xffe0e0e0))
    ) {}
}

data class WasherPanelUi(
    val dispenserPanelUi: DispenserPanelUi,
    val controlPanelUi: ControlPanelUi,
    val statusPanelUi: StatusPanelUi?,
    val drumUi: DrumUi
) {
    companion object {
        @Composable
        fun preview() = WasherPanelUi(
            dispenserPanelUi = DispenserPanelUi.preview(),
            controlPanelUi = ControlPanelUi.preview(),
            statusPanelUi = StatusPanelUi.preview(),
            drumUi = DrumUi.preview()
        )
    }
}

@Preview
@Composable
private fun PreviewWasherPanel() {
    AppTheme {
        WasherPanel(WasherPanelUi.preview())
    }
}
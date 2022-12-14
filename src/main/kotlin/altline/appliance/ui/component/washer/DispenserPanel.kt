package altline.appliance.ui.component.washer

import altline.appliance.ui.theme.AppTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun DispenserPanel(
    data: DispenserPanelUi,
    modifier: Modifier = Modifier
) {
    Box(modifier.clickable(onClick = data.onClick)) {
        Surface(
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.05f),
            shape = CutCornerShape(bottomStartPercent = 100, bottomEndPercent = 100),
            color = Color.LightGray
        ) {}
    }
}

data class DispenserPanelUi(
    val onClick: () -> Unit
) {
    companion object {
        @Composable
        fun preview() = DispenserPanelUi(
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun PreviewDispenserPanel() {
    AppTheme {
        DispenserPanel(DispenserPanelUi.preview())
    }
}
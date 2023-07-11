package altline.appliance.ui.component.washer

import altline.appliance.ui.resources.AppIcons
import altline.appliance.ui.resources.appicons.PreWash
import altline.appliance.ui.resources.appicons.Spin
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.AppTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun StatusPanel(
    data: StatusPanelUi?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .padding(horizontal = 8.dp, vertical = 24.dp)
            .border(1.dp, Color.LightGray)
            .padding(12.dp)
    ) {
        if (data != null) {
            if (data.preWash) {
                Icon(
                    imageVector = AppIcons.PreWash,
                    contentDescription = strings["preWash_contentDesc"]
                )
            }

            Text(
                text = data.timer,
                Modifier.align(Alignment.TopEnd),
                style = MaterialTheme.typography.h5
            )

            Row(
                Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Indicator(
                    icon = Icons.Default.DeviceThermostat,
                    contentDescription = strings["temperature_contentDesc"],
                    value = data.temperature
                )
                Indicator(
                    icon = AppIcons.Spin,
                    contentDescription = strings["spinSpeed_contentDesc"],
                    value = data.spinSpeed
                )
            }
        }
    }
}

@Composable
private fun Indicator(
    icon: ImageVector,
    contentDescription: String,
    value: String
) {
    Row {
        Icon(icon, contentDescription)
        Text(value)
    }
}

data class StatusPanelUi(
    val timer: String,
    val temperature: String,
    val spinSpeed: String,
    val preWash: Boolean
) {
    companion object {
        @Composable
        fun preview() = StatusPanelUi(
            timer = "3:20",
            temperature = "40°C",
            spinSpeed = "1000",
            preWash = true
        )
    }
}

@Preview
@Composable
private fun PreviewStatusPanel() {
    AppTheme {
        StatusPanel(StatusPanelUi.preview())
    }
}
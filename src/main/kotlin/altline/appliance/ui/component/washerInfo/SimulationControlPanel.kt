package altline.appliance.ui.component.washerInfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#.##")

@Composable
fun SimulationControlPanel(
    data: SimulationControlPanelUi,
    modifier: Modifier = Modifier
) {
    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        data.speedSettings.forEach { speed ->
            SpeedButton(
                speedModifier = speed,
                checked = speed == data.selectedSpeed,
                onClick = data.onSpeedClick,
                modifier = Modifier.weight(1f, fill = false)
            )
        }
    }
}

@Composable
private fun SpeedButton(
    speedModifier: Float,
    checked: Boolean,
    onClick: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { onClick(speedModifier) },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (checked) MaterialTheme.colors.primaryVariant else MaterialTheme.colors.primary
        ),
        modifier = modifier
    ) {
        Text("x${decimalFormat.format(speedModifier)}")
    }
}

data class SimulationControlPanelUi(
    val speedSettings: List<Float>,
    val selectedSpeed: Float,
    val onSpeedClick: (Float) -> Unit
)
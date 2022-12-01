package altline.appliance.ui.component.washer

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InfoPanel(
    data: InfoPanelUi,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = data.washCycleName
        )
    }
}

data class InfoPanelUi(
    val washCycleName: String
) {
    companion object {
        @Composable
        fun preview() = InfoPanelUi(
            washCycleName = "Rinse Cycle"
        )
    }
}
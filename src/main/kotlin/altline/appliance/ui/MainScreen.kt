package altline.appliance.ui

import altline.appliance.ui.component.laundry.LaundryPanel
import altline.appliance.ui.component.laundry.LaundryPanelUi
import altline.appliance.ui.component.washer.InfoPanel
import altline.appliance.ui.component.washer.InfoPanelUi
import altline.appliance.ui.theme.AppTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(viewModel: MainViewModel) {
    with(viewModel.uiState) {
        Content(laundryPanel, infoPanel)
    }
}

@Composable
private fun Content(
    laundry: LaundryPanelUi?,
    washer: InfoPanelUi?
) {
    Row {
        if (laundry != null) {
            LaundryPanel(laundry, Modifier.weight(0.5f))
        }
        if (washer != null) {
            InfoPanel(washer, Modifier.weight(0.5f))
        }
    }
}

@Preview
@Composable
private fun PreviewContent() {
    AppTheme {
        Content(
            LaundryPanelUi.preview(),
            InfoPanelUi.preview()
        )
    }
}

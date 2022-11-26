package altline.appliance.ui

import altline.appliance.ui.component.LaundryPanel
import altline.appliance.ui.component.LaundryPanelUi
import altline.appliance.ui.component.InfoPanel
import altline.appliance.ui.component.InfoPanelUi
import altline.appliance.ui.theme.AppTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable

@Composable
fun MainScreen(viewModel: MainViewModel) {
    with(viewModel.uiState) {
        Content(laundry, info)
    }
}

@Composable
private fun Content(
    laundry: LaundryPanelUi?,
    washer: InfoPanelUi?
) {
    Row {
        if (laundry != null) {
            LaundryPanel(laundry)
        }
        if (washer != null) {
            InfoPanel(washer)
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

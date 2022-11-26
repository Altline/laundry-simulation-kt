package altline.appliance.ui.component

import altline.appliance.ui.theme.AppTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

@Composable
fun LaundryPanel(data: LaundryPanelUi) {
    Column {
        LaundryList(data.potentialLaundry)
        LaundryList(data.loadedLaundry)
    }
}

@Composable
private fun LaundryList(data: List<LaundryListItemUi>) {
    LazyColumn {
        items(data) {
            LaundryListItem(it)
        }
    }
}

data class LaundryPanelUi(
    val potentialLaundry: List<LaundryListItemUi>,
    val loadedLaundry: List<LaundryListItemUi>
) {
    companion object {
        @Composable
        fun preview() = LaundryPanelUi(
            potentialLaundry = listOf(
                LaundryListItemUi.preview(),
                LaundryListItemUi.preview(),
                LaundryListItemUi.preview()
            ),
            loadedLaundry = listOf(
                LaundryListItemUi.preview(),
                LaundryListItemUi.preview()
            )
        )
    }
}

@Composable
@Preview
private fun PreviewLaundryPanel() {
    AppTheme {
        LaundryPanel(LaundryPanelUi.preview())
    }
}
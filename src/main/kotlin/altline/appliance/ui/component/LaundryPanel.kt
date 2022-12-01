package altline.appliance.ui.component

import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.theme.surfaceTinted
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LaundryPanel(
    data: LaundryPanelUi,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxHeight()
            .background(MaterialTheme.colors.surfaceTinted)
            .padding(8.dp)
    ) {
        LaundryList(data.potentialLaundry)
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { data.onTransferClick?.invoke() },
                enabled = data.onTransferClick != null
            ) {
                Icon(Icons.Default.SwapVert, contentDescription = null)
            }
        }
        LaundryList(data.loadedLaundry)
    }
}

@Composable
private fun ColumnScope.LaundryList(data: List<LaundryListItemUi>) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .weight(0.4f)
            .background(Color.White)
    ) {
        items(data) {
            LaundryListItem(it)
            Divider()
        }
    }
}

data class LaundryPanelUi(
    val potentialLaundry: List<LaundryListItemUi>,
    val loadedLaundry: List<LaundryListItemUi>,
    val onTransferClick: (() -> Unit)?
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
            ),
            onTransferClick = null
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
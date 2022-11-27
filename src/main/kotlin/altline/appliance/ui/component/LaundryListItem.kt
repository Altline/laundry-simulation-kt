package altline.appliance.ui.component

import altline.appliance.ui.resources.AppIcons
import altline.appliance.ui.resources.appicons.Stain
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.theme.onSurfaceTinted
import altline.appliance.ui.util.formatPercentage
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun LaundryListItem(data: LaundryListItemUi) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
    ) {
        val barHeight: Float
        if (data.soakRatio != null) {
            barHeight = 0.5f
            Surface(
                Modifier
                    .fillMaxWidth(data.soakRatio.toFloat())
                    .fillMaxHeight(0.5f)
                    .align(Alignment.TopStart),
                color = Color(0xffbfe1ff)
            ) {}
        } else {
            barHeight = 1f
        }
        Surface(
            Modifier
                .fillMaxWidth(data.stainRatio.toFloat())
                .fillMaxHeight(barHeight)
                .align(Alignment.BottomStart),
            color = Color(0xffffdfbf)
        ) {}

        Column(
            Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = data.title
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconStat(
                    icon = AppIcons.Stain,
                    contentDescription = strings["stainPercentage"],
                    text = formatPercentage(data.stainRatio)
                )
                if (data.soakRatio != null) {
                    Spacer(Modifier.width(16.dp))
                    IconStat(
                        icon = Icons.Default.WaterDrop,
                        contentDescription = strings["soakPercentage"],
                        text = formatPercentage(data.soakRatio)
                    )
                }
            }
        }
    }
}

@Composable
private fun RowScope.IconStat(
    icon: ImageVector,
    contentDescription: String,
    text: String
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = MaterialTheme.colors.onSurfaceTinted
    )
    Text(
        text = text
    )
}

data class LaundryListItemUi(
    val title: String,
    val stainRatio: Double,
    val soakRatio: Double?
) {
    companion object {
        @Composable
        fun preview() = LaundryListItemUi(
            title = "Shirt",
            stainRatio = 0.13,
            soakRatio = 0.88
        )
    }
}

@Composable
@Preview
private fun PreviewLaundryListItem() {
    AppTheme {
        LaundryListItem(LaundryListItemUi.preview())
    }
}
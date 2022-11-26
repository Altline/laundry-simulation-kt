package altline.appliance.ui.component

import altline.appliance.ui.theme.AppTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun LaundryListItem(data: LaundryListItemUi) {
    Box(
        Modifier
            .width(IntrinsicSize.Max)
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

        Column {
            Text(
                text = data.title
            )
            Row {
                IconStat(
                    icon = Icons.Default.Face,
                    contentDescription = "Stain percentage",
                    text = "${data.stainRatio * 100} %"
                )
                if (data.soakRatio != null) {
                    IconStat(
                        icon = Icons.Default.Face,
                        contentDescription = "Soak percentage", // TODO resource
                        text = "${data.soakRatio * 100} %"
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
        contentDescription = contentDescription
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
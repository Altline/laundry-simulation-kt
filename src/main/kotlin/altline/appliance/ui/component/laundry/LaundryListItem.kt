package altline.appliance.ui.component.laundry

import altline.appliance.ui.component.VerticalDivider
import altline.appliance.ui.resources.AppIcons
import altline.appliance.ui.resources.appicons.Stain
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.util.formatPercentage
import altline.appliance.ui.util.modifiedColor
import altline.appliance.util.lerpCoerced
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LaundryListItem(data: LaundryListItemUi) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .onClick(
                onClick = data.onClick,
                // adding onDoubleClick delays the call of onClick, which is horrible, hoping for a change soon
                //onDoubleClick = data.onDoubleClick
            ),
    ) {
        BarMeters(data.soakRatio, data.stainRatio, data.freshness)

        Column(
            Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = data.title,
                color = modifiedColor(alpha = ContentAlpha.medium),
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconStat(
                    icon = AppIcons.Stain,
                    contentDescription = strings["stainPercentage"],
                    text = formatPercentage(data.stainRatio),
                    tint = Color(0xff7a5928),
                    modifier = Modifier.widthIn(min = 100.dp)
                )
                if (data.soakRatio != null) {
                    Spacer(Modifier.width(16.dp))
                    IconStat(
                        icon = Icons.Default.WaterDrop,
                        contentDescription = strings["soakPercentage"],
                        text = formatPercentage(data.soakRatio),
                        tint = Color(0xff2a698e)
                    )
                }
            }
        }

        if (data.selected) {
            Surface(
                Modifier.fillMaxSize(),
                color = Color(0x260099ff)
            ) {}
        }
    }
}

@Composable
private fun BoxScope.BarMeters(
    soakRatio: Double?,
    stainRatio: Double,
    freshness: Double
) {
    val freshnessBarWidthFraction = 0.1f
    val barHeight: Float

    if (soakRatio != null) {
        barHeight = 0.5f
        Surface(
            Modifier
                .fillMaxWidth(lerpCoerced(0f, 1 - freshnessBarWidthFraction, soakRatio.toFloat()))
                .fillMaxHeight(0.5f)
                .align(Alignment.TopStart),
            color = Color(0xffbfe1ff)
        ) {}
    } else {
        barHeight = 1f
    }

    Surface(
        Modifier
            .fillMaxWidth(lerpCoerced(0f, 1 - freshnessBarWidthFraction, stainRatio.toFloat()))
            .fillMaxHeight(barHeight)
            .align(Alignment.BottomStart),
        color = Color(0xffffdfbf)
    ) {}

    Row(
        Modifier.align(Alignment.BottomEnd),
        verticalAlignment = Alignment.Bottom
    ) {
        VerticalDivider()
        Surface(
            Modifier
                .fillMaxWidth(freshnessBarWidthFraction)
                .fillMaxHeight(freshness.toFloat()),
            color = Color(0xff7fbeff)
        ) {}
    }
}

@Composable
private fun IconStat(
    icon: ImageVector,
    contentDescription: String,
    text: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint
        )
        Text(text)
    }
}

data class LaundryListItemUi(
    val id: UUID,
    val title: String,
    val stainRatio: Double,
    val soakRatio: Double?,
    val freshness: Double,
    val selected: Boolean,
    val onClick: () -> Unit,
    val onDoubleClick: () -> Unit
) {
    companion object {
        @Composable
        fun preview() = LaundryListItemUi(
            id = UUID.randomUUID(),
            title = "Shirt",
            stainRatio = 0.13,
            soakRatio = 0.88,
            freshness = 0.4,
            selected = false,
            onClick = {},
            onDoubleClick = {}
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
package altline.appliance.ui.component

import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.theme.surfaceTinted
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextRow(
    leftText: String,
    rightText: String,
    modifier: Modifier = Modifier,
    textSettings: TextRowSettings = TextRowSettings.default(),
    fillWhitespace: Boolean = false
) = TextRow(leftText, rightText, modifier, textSettings, textSettings, fillWhitespace)

@Composable
fun TextRow(
    leftText: String,
    rightText: String,
    modifier: Modifier = Modifier,
    leftTextSettings: TextRowSettings = TextRowSettings.default(),
    rightTextSettings: TextRowSettings = TextRowSettings.default(),
    fillWhitespace: Boolean = false
) {
    Row(
        Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = leftText,
            modifier = leftTextSettings.verticalAlignment?.let { Modifier.align(it) } ?: Modifier,
            color = leftTextSettings.color,
            style = leftTextSettings.style,
            textAlign = TextAlign.Left,
            maxLines = 2
        )
        if (fillWhitespace) {
            Box(
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .padding(horizontal = 4.dp)
                    .align(Alignment.Bottom)
                    .background(MaterialTheme.colors.surfaceTinted)
            )
        }
        Text(
            text = rightText,
            modifier = rightTextSettings.verticalAlignment?.let { Modifier.align(it) } ?: Modifier,
            color = rightTextSettings.color,
            style = rightTextSettings.style,
            textAlign = TextAlign.Right
        )
    }
}

data class TextRowSettings(
    val color: Color,
    val style: TextStyle,
    val verticalAlignment: Alignment.Vertical?
) {
    companion object {
        @Composable
        fun default(
            color: Color = MaterialTheme.colors.onSurface,
            style: TextStyle = MaterialTheme.typography.body2,
            verticalAlignment: Alignment.Vertical? = null
        ) = TextRowSettings(color, style, verticalAlignment)
    }
}

@Composable
@Preview
private fun PreviewTextRow() {
    AppTheme {
        TextRow(
            "Left text", "Right text",
            fillWhitespace = true
        )
    }
}

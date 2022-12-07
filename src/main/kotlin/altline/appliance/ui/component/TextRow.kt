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
    settings: TextRowSettings = TextRowSettings.default()
) {
    Row(
        Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = leftText,
            color = settings.leftTextColor,
            style = settings.leftTextStyle,
            textAlign = TextAlign.Left
        )
        if (settings.fillWhitespace) {
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
            color = settings.rightTextColor,
            style = settings.rightTextStyle,
            textAlign = TextAlign.Right
        )
    }
}

data class TextRowSettings(
    val leftTextColor: Color,
    val leftTextStyle: TextStyle,
    val rightTextColor: Color,
    val rightTextStyle: TextStyle,
    val fillWhitespace: Boolean
) {
    companion object {
        @Composable
        fun default(
            leftTextColor: Color = MaterialTheme.colors.onSurface,
            leftTextStyle: TextStyle = MaterialTheme.typography.body2,
            rightTextColor: Color = MaterialTheme.colors.onSurface,
            rightTextStyle: TextStyle = MaterialTheme.typography.body2,
            fillWhitespace: Boolean = false
        ) = TextRowSettings(leftTextColor, leftTextStyle, rightTextColor, rightTextStyle, fillWhitespace)
    }
}

@Composable
@Preview
private fun PreviewTextRow() {
    AppTheme {
        TextRow("Left text", "Right text", settings = TextRowSettings.default(fillWhitespace = true))
    }
}

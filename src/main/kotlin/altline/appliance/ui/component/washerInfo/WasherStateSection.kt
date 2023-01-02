package altline.appliance.ui.component.washerInfo

import altline.appliance.common.RefreshPeriod
import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.delay
import altline.appliance.ui.component.TextRow
import altline.appliance.ui.component.TextRowSettings
import altline.appliance.ui.component.VerticalDivider
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.util.animateRpmAsState
import altline.appliance.ui.util.modifiedColor
import altline.appliance.ui.util.optionalDecimal
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.nacular.measured.units.Measure
import io.nacular.measured.units.times

@Composable
fun WasherStateSection(data: WasherStateSectionUi) {
    val rpm = (data.spinSpeed `in` rpm).toFloat()
    val spinSpeed by animateRpmAsState(rpm)
    var shownRpm by remember { mutableStateOf(data.spinSpeed.optionalDecimal()) }

    LaunchedEffect(data.spinSpeed) {
        while (spinSpeed != rpm) {
            shownRpm = Measure(spinSpeed.toDouble(), Spin.rpm).optionalDecimal()
            delay(RefreshPeriod)
        }
        shownRpm = data.spinSpeed.optionalDecimal()
    }

    Column(
        Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TextRow(
            leftText = strings["washerState_spinSpeed"],
            rightText = shownRpm
        )
        TextRow(
            leftText = strings["washerState_temperature"],
            rightText = data.temperature
        )
        TextRow(
            leftText = strings["washerState_waterLevel"],
            rightText = data.liquidLevel
        )
        LiquidParts(data.liquidParts)
    }
}

@Composable
private fun LiquidParts(parts: List<Pair<String, String>>) {
    Row(Modifier.height(IntrinsicSize.Max)) {
        VerticalDivider(Modifier.padding(horizontal = 8.dp))
        Column {
            for (part in parts) {
                TextRow(
                    leftText = part.first,
                    rightText = part.second,
                    textSettings = TextRowSettings.default(
                        color = modifiedColor(alpha = ContentAlpha.medium),
                        style = MaterialTheme.typography.caption
                    ),
                    fillWhitespace = true
                )
            }
        }
    }
}

data class WasherStateSectionUi(
    val spinSpeed: Measure<Spin>,
    val temperature: String,
    val liquidLevel: String,
    val liquidParts: List<Pair<String, String>>
) {
    companion object {
        @Composable
        fun preview() = WasherStateSectionUi(
            spinSpeed = 1000 * rpm,
            temperature = "20 °C",
            liquidLevel = "10.5 L",
            liquidParts = listOf(
                "Water" to "10.4 L",
                "Coffee" to "106 mL"
            )
        )
    }
}

@Preview
@Composable
private fun PreviewWasherStateSection() {
    AppTheme {
        WasherStateSection(WasherStateSectionUi.preview())
    }
}
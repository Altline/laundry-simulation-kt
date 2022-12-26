package altline.appliance.ui.component.washerInfo

import altline.appliance.common.RefreshPeriod
import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.delay
import altline.appliance.ui.component.TextRow
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.util.animateRpmAsState
import altline.appliance.ui.util.optionalDecimal
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
            leftText = strings["washerState_waterLevel"],
            rightText = data.waterLevel
        )
    }
}

data class WasherStateSectionUi(
    val spinSpeed: Measure<Spin>,
    val waterLevel: String
) {
    companion object {
        @Composable
        fun preview() = WasherStateSectionUi(
            spinSpeed = 1000 * rpm,
            waterLevel = "10.5 L"
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
package altline.appliance.ui.component.washerInfo

import altline.appliance.ui.component.TextRow
import altline.appliance.ui.component.TextRowSettings
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.util.modifiedColor
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun WashCycleSection(data: WashCycleSectionUi) {
    Column {
        TextRow(
            leftText = data.cycleName,
            rightText = data.timer,
            Modifier.padding(8.dp),
            settings = TextRowSettings.default(
                leftTextStyle = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                rightTextStyle = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
            )
        )
        Divider()
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            for (phase in data.phases) {
                CyclePhase(phase)
            }
        }
        Divider()
    }
}

@Composable
private fun CyclePhase(data: CyclePhaseUi) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val iconModifier = Modifier
                .padding(end = 4.dp)
                .size(8.dp)
            if (data.active) {
                Icon(
                    Icons.Filled.Circle,
                    contentDescription = strings["active"],
                    iconModifier,
                    tint = Color.Green
                )
            } else {
                Icon(
                    Icons.Outlined.Circle,
                    contentDescription = strings["inactive"],
                    iconModifier,
                    tint = Color.Gray
                )
            }
            TextRow(
                leftText = data.name,
                rightText = data.timer,
                settings = TextRowSettings.default(
                    leftTextStyle = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold),
                    rightTextStyle = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold)
                )
            )
        }
        for (section in data.sections) {
            PhaseSection(section)
        }
    }
}

@Composable
private fun PhaseSection(data: PhaseSectionUi) {
    Column(
        Modifier.padding(start = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("> ", Modifier.alpha(if (data.active) 1f else 0f))
            TextRow(
                leftText = data.name,
                rightText = data.timer
            )
        }
        if (data.params != null) {
            WashParams(data.params)
        }
    }
}

@Composable
private fun WashParams(data: WashParamsUi) {
    Column(
        Modifier.padding(start = 24.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        val textRowSettings = TextRowSettings.default(
            leftTextColor = modifiedColor(alpha = ContentAlpha.medium),
            leftTextStyle = MaterialTheme.typography.caption,
            rightTextColor = modifiedColor(alpha = ContentAlpha.medium),
            rightTextStyle = MaterialTheme.typography.caption,
            fillWhitespace = true
        )
        TextRow(
            leftText = strings["washParams_spinPeriod"],
            rightText = data.spinPeriod,
            settings = textRowSettings
        )
        TextRow(
            leftText = strings["washParams_restPeriod"],
            rightText = data.restPeriod,
            settings = textRowSettings
        )
        TextRow(
            leftText = strings["washParams_spinSpeed"],
            rightText = data.spinSpeed,
            settings = textRowSettings
        )
    }
}

data class CyclePhaseUi(
    val name: String,
    val timer: String,
    val sections: List<PhaseSectionUi>,
    val active: Boolean
)

data class PhaseSectionUi(
    val name: String,
    val timer: String,
    val params: WashParamsUi?,
    val active: Boolean
)

data class WashParamsUi(
    val spinPeriod: String,
    val restPeriod: String,
    val spinSpeed: String
)

data class WashCycleSectionUi(
    val cycleName: String,
    val timer: String,
    val phases: List<CyclePhaseUi>
) {
    companion object {
        @Composable
        fun preview() = WashCycleSectionUi(
            cycleName = "Cotton cycle",
            timer = "0:00:13 / 0:01:42",
            phases = listOf(
                CyclePhaseUi(
                    name = "Fill",
                    timer = "0:01:35 / -",
                    sections = emptyList(),
                    active = false
                ),
                CyclePhaseUi(
                    name = "Wash",
                    timer = "0:02:45 / 0:47:30",
                    sections = listOf(
                        PhaseSectionUi(
                            name = "Section 1",
                            timer = "0:10:30",
                            params = WashParamsUi(
                                spinPeriod = "5 s",
                                restPeriod = "5 s",
                                spinSpeed = "60 rpm",
                            ),
                            active = false
                        ),
                        PhaseSectionUi(
                            name = "Section 2",
                            timer = "0:02:45 / 0:37:00",
                            params = WashParamsUi(
                                spinPeriod = "14 s",
                                restPeriod = "7 s",
                                spinSpeed = "50 rpm",
                            ),
                            active = true
                        )
                    ),
                    active = true
                )
            )
        )
    }
}

@Composable
@Preview
private fun PreviewWashCycleSection() {
    AppTheme {
        WashCycleSection(WashCycleSectionUi.preview())
    }
}
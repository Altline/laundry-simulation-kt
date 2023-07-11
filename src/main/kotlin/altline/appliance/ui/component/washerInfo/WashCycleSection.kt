package altline.appliance.ui.component.washerInfo

import altline.appliance.ui.component.TextRow
import altline.appliance.ui.component.TextRowSettings
import altline.appliance.ui.component.washerInfo.CyclePhaseUi.ActiveState.*
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun WashCycleSection(
    data: WashCycleSectionUi,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        TextRow(
            leftText = data.cycleName,
            rightText = data.timer,
            Modifier.padding(8.dp),
            textSettings = TextRowSettings.default(
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
            )
        )
        Divider()
        Column(
            Modifier
                .verticalScroll(rememberScrollState())
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            data.stages.forEachIndexed { index, stage ->
                CycleStage(stage)
                if (index != data.stages.lastIndex) Divider()
            }
        }
    }
}

@Composable
private fun CycleStage(data: CycleStageUi) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        for (phase in data.phases) {
            CyclePhase(phase)
        }
    }
}

@Composable
private fun CyclePhase(data: CyclePhaseUi) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val iconModifier = Modifier
                .padding(end = 4.dp)
                .size(8.dp)

            when (data.active) {
                NOT_EXECUTED -> {
                    Icon(
                        Icons.Outlined.Circle,
                        contentDescription = strings["notExecuted"],
                        iconModifier,
                        tint = Color.Gray
                    )
                }

                EXECUTED -> {
                    Icon(
                        Icons.Filled.Circle,
                        contentDescription = strings["executed"],
                        iconModifier,
                        tint = Color.Gray
                    )
                }

                ACTIVE -> {
                    Icon(
                        Icons.Filled.Circle,
                        contentDescription = strings["active"],
                        iconModifier,
                        tint = Color.Green
                    )
                }
            }

            val textRowSettings = TextRowSettings.default(
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.SemiBold),
                verticalAlignment = Alignment.CenterVertically
            )

            TextRow(
                leftText = data.name,
                rightText = data.timer,
                leftTextSettings = if (data.disabled) textRowSettings.copy(decoration = TextDecoration.LineThrough) else textRowSettings,
                rightTextSettings = textRowSettings
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
            Column(
                Modifier.padding(start = 24.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                for (param in data.params) {
                    TextRow(
                        leftText = param.name,
                        rightText = param.value,
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
}

data class CycleStageUi(
    val phases: List<CyclePhaseUi>
)

data class CyclePhaseUi(
    val name: String,
    val timer: String,
    val sections: List<PhaseSectionUi>,
    val active: ActiveState,
    val disabled: Boolean
) {
    enum class ActiveState {
        NOT_EXECUTED, ACTIVE, EXECUTED
    }
}

data class PhaseSectionUi(
    val name: String,
    val timer: String,
    val params: List<SectionParamUi>?,
    val active: Boolean
)

data class SectionParamUi(
    val name: String,
    val value: String
)

data class WashCycleSectionUi(
    val cycleName: String,
    val timer: String,
    val stages: List<CycleStageUi>
) {
    companion object {
        @Composable
        fun preview() = WashCycleSectionUi(
            cycleName = "Cotton cycle",
            timer = "0:00:13 / 0:01:42",
            stages = listOf(
                CycleStageUi(
                    phases = listOf(
                        CyclePhaseUi(
                            name = "Fill",
                            timer = "0:01:35 / -",
                            sections = emptyList(),
                            active = NOT_EXECUTED,
                            disabled = false
                        ),
                        CyclePhaseUi(
                            name = "Wash",
                            timer = "0:02:45 / 0:47:30",
                            sections = listOf(
                                PhaseSectionUi(
                                    name = "Section 1",
                                    timer = "0:10:30",
                                    params = listOf(
                                        SectionParamUi(
                                            name = "name",
                                            value = "value"
                                        )
                                    ),
                                    active = false
                                ),
                                PhaseSectionUi(
                                    name = "Section 2",
                                    timer = "0:02:45 / 0:37:00",
                                    params = null,
                                    active = true
                                )
                            ),
                            active = ACTIVE,
                            disabled = false
                        )
                    )
                ),
                CycleStageUi(
                    phases = listOf(
                        CyclePhaseUi(
                            name = "Fill",
                            timer = "0:01:35 / -",
                            sections = emptyList(),
                            active = NOT_EXECUTED,
                            disabled = true
                        )
                    )
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
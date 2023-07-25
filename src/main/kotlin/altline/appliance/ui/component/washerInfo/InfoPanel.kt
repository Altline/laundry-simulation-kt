package altline.appliance.ui.component.washerInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InfoPanel(
    data: InfoPanelUi,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxHeight()) {
        WashCycleSection(
            data.cycleSectionUi,
            Modifier.weight(1f)
        )
        Divider()
        if (data.washerStateSectionUi != null) {
            WasherStateSection(data.washerStateSectionUi)
        }
        Divider()
        SimulationControlPanel(data.simulationControlPanelUi)
    }
}

data class InfoPanelUi(
    val cycleSectionUi: WashCycleSectionUi,
    val washerStateSectionUi: WasherStateSectionUi?,
    val simulationControlPanelUi: SimulationControlPanelUi
) {
    companion object {
        @Composable
        fun preview() = InfoPanelUi(
            cycleSectionUi = WashCycleSectionUi.preview(),
            washerStateSectionUi = WasherStateSectionUi.preview(),
            simulationControlPanelUi = SimulationControlPanelUi(
                speedSettings = listOf(1f, 2f, 10f, 20f),
                selectedSpeed = 1f,
                onSpeedClick = {}
            )
        )
    }
}
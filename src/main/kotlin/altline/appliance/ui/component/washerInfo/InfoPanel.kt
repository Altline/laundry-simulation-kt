package altline.appliance.ui.component.washerInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InfoPanel(
    data: InfoPanelUi,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxHeight()) {
        WashCycleSection(data.cycleSectionUi)
        if (data.washerStateSectionUi != null) {
            WasherStateSection(data.washerStateSectionUi)
        }
    }
}

data class InfoPanelUi(
    val cycleSectionUi: WashCycleSectionUi,
    val washerStateSectionUi: WasherStateSectionUi?
) {
    companion object {
        @Composable
        fun preview() = InfoPanelUi(
            cycleSectionUi = WashCycleSectionUi.preview(),
            washerStateSectionUi = WasherStateSectionUi.preview()
        )
    }
}
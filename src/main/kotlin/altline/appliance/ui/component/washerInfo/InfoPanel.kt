package altline.appliance.ui.component.washerInfo

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InfoPanel(
    data: InfoPanelUi,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        WashCycleSection(data.cycleSectionUi)
    }
}

data class InfoPanelUi(
    val cycleSectionUi: WashCycleSectionUi
) {
    companion object {
        @Composable
        fun preview() = InfoPanelUi(
            cycleSectionUi = WashCycleSectionUi.preview()
        )
    }
}
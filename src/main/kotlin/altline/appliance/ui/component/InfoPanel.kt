package altline.appliance.ui.component

import androidx.compose.runtime.Composable

@Composable
fun InfoPanel(data: InfoPanelUi) {

}

data class InfoPanelUi(
    val washCycleName: String
) {
    companion object {
        @Composable
        fun preview() = InfoPanelUi(
            washCycleName = "Rinse Cycle"
        )
    }
}
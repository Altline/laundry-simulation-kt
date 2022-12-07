package altline.appliance.ui

import altline.appliance.ui.component.laundry.LaundryPanelUi
import altline.appliance.ui.component.washerInfo.InfoPanelUi

data class MainUiState(
    val laundryPanel: LaundryPanelUi,
    val infoPanel: InfoPanelUi
)
package altline.appliance.ui

import altline.appliance.ui.component.LaundryPanelUi
import altline.appliance.ui.component.InfoPanelUi

data class MainUiState(
    val laundryPanel: LaundryPanelUi? = null,
    val infoPanel: InfoPanelUi? = null
)
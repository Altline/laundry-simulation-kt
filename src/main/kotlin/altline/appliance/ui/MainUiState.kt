package altline.appliance.ui

import altline.appliance.ui.component.laundry.LaundryPanelUi
import altline.appliance.ui.component.washer.InfoPanelUi

data class MainUiState(
    val laundryPanel: LaundryPanelUi? = null,
    val infoPanel: InfoPanelUi? = null
)
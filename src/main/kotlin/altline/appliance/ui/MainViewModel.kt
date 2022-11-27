package altline.appliance.ui

import altline.appliance.data.World
import altline.appliance.ui.mapper.LaundryMapper
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainViewModel(
    private val world: World,
    private val laundryMapper: LaundryMapper
) {

    var uiState by mutableStateOf(MainUiState())
        private set

    init {
        uiState = MainUiState(
            laundryPanel = laundryMapper.mapToLaundryPanel(
                potentialLaundry = world.laundry.minus(world.washer.load),
                loadedLaundry = world.washer.load
            ),
            infoPanel = laundryMapper.mapToInfoPanel(world.washer)
        )
    }
}
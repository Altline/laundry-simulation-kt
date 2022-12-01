package altline.appliance.ui

import altline.appliance.common.Body
import altline.appliance.data.World
import altline.appliance.ui.component.LaundryListItemUi
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

    private var selectedLaundryItem: Body? = null

    private val loadedLaundry: Set<Body>
        get() = world.washer.load

    private val potentialLaundry: Set<Body>
        get() = world.laundry.minus(loadedLaundry)

    init {
        loadData()
    }

    private fun loadData() {
        uiState = MainUiState(
            laundryPanel = laundryMapper.mapToLaundryPanel(
                potentialLaundry = potentialLaundry,
                loadedLaundry = loadedLaundry,
                selectedLaundryItem = selectedLaundryItem,
                onItemClick = this::selectLaundryItem,
                onItemDoubleClick = { transferLaundryItem(it, selectNext = false) },
                onTransferClick = { selectedLaundryItem?.let { transferLaundryItem(it, selectNext = true) } }
            ),
            infoPanel = laundryMapper.mapToInfoPanel(world.washer)
        )
    }

    private fun selectLaundryItem(item: Body) {
        selectedLaundryItem = if (selectedLaundryItem == item) null else item
        loadData()
    }

    private fun transferLaundryItem(item: Body, selectNext: Boolean) {
        selectedLaundryItem = if (selectNext) findNextSelection(item) else item
        with(world) {
            if (item in washer.load) washer.unload(item)
            else washer.load(item)
        }
        loadData()
    }

    /**
     * Finds the laundry item that should receive the selection after the given item is transferred.
     * **Must be called before the transfer is made.**
     */
    private fun findNextSelection(item: Body): Body? {
        val uiPotentialLaundry = uiState.laundryPanel!!.potentialLaundry
        val uiLoadedLaundry = uiState.laundryPanel!!.loadedLaundry
        var owningList: List<LaundryListItemUi> = emptyList()
        val listItem = uiPotentialLaundry.find { it.id == item.id }
            ?.also { owningList = uiPotentialLaundry }
            ?: uiLoadedLaundry.find { it.id == item.id }
                ?.also { owningList = uiLoadedLaundry }
        val index = owningList.indexOf(listItem)
        val nextSelectedIndex = when {
            owningList.size == 1 -> null
            index == owningList.size - 1 -> index - 1
            else -> index + 1
        }
        val nextSelectedId = nextSelectedIndex?.let { owningList[nextSelectedIndex].id }
        return nextSelectedId?.let { world.laundry.find { it.id == nextSelectedId } }
    }
}
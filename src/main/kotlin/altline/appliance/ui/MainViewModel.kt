package altline.appliance.ui

import altline.appliance.common.Body
import altline.appliance.data.World
import altline.appliance.ui.component.laundry.LaundryListItemUi
import altline.appliance.ui.component.laundry.LaundryPanelUi
import altline.appliance.ui.component.washer.WasherPanelUi
import altline.appliance.ui.component.washerInfo.InfoPanelUi
import altline.appliance.ui.mapper.LaundryMapper
import altline.appliance.ui.mapper.WasherInfoMapper
import altline.appliance.ui.mapper.WasherMapper
import altline.appliance.util.wrapAround
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MainViewModel(
    private val world: World,
    private val laundryMapper: LaundryMapper,
    private val washerMapper: WasherMapper,
    private val washerInfoMapper: WasherInfoMapper
) {

    var uiState by mutableStateOf<MainUiState?>(null)
        private set

    private var selectedLaundryItem: Body? = null

    private val loadedLaundry: Set<Body>
        get() = world.washer.load

    private val potentialLaundry: Set<Body>
        get() = world.laundry.minus(loadedLaundry)

    init {
        updateData()
    }

    private fun updateData() {
        uiState = MainUiState(
            laundryPanel = getLaundryPanel(),
            washerPanel = getWasherPanel(),
            infoPanel = getInfoPanel()
        )
    }

    private fun updateWasherData() {
        uiState = uiState?.copy(
            washerPanel = getWasherPanel(),
            infoPanel = getInfoPanel()
        )
    }

    private fun getLaundryPanel(): LaundryPanelUi {
        return laundryMapper.mapToLaundryPanel(
            potentialLaundry = potentialLaundry,
            loadedLaundry = loadedLaundry,
            selectedLaundryItem = selectedLaundryItem,
            onItemClick = this::selectLaundryItem,
            onItemDoubleClick = { transferLaundryItem(it, selectNext = false) },
            onTransferClick = { selectedLaundryItem?.let { transferLaundryItem(it, selectNext = true) } }
        )
    }

    private fun getWasherPanel(): WasherPanelUi {
        return washerMapper.mapToWasherPanel(
            world.washer,
            onDispenserClick = this::openDispenser,
            onSelectNextCycle = this::selectNextCycle,
            onPowerOnOff = this::togglePower,
            onStartPause = this::toggleCycleRun,
            onTempUp = {},
            onTempDown = {},
            onRpmUp = {},
            onRpmDown = {}
        )
    }

    private fun getInfoPanel(): InfoPanelUi {
        return washerInfoMapper.mapToInfoPanel(world.washer)
    }

    private fun selectLaundryItem(item: Body) {
        selectedLaundryItem = if (selectedLaundryItem == item) null else item
        updateData()
    }

    private fun transferLaundryItem(item: Body, selectNext: Boolean) {
        selectedLaundryItem = if (selectNext) findNextSelection(item) else item
        with(world) {
            if (item in washer.load) washer.unload(item)
            else washer.load(item)
        }
        updateData()
    }

    private fun openDispenser() {

    }

    private fun selectNextCycle(reverse: Boolean) {
        with(world.washer) {
            val currentCycleIndex = washCycles.indexOf(selectedWashCycle)
            val nextCycleIndex = kotlin.run {
                if (reverse) currentCycleIndex - 1
                else currentCycleIndex + 1
            }.wrapAround(washCycles.indices)

            println(nextCycleIndex)
            selectedWashCycle = washCycles[nextCycleIndex]
        }
        updateWasherData()
    }

    private fun togglePower() {
        world.washer.togglePower()
        updateWasherData()
    }

    private fun toggleCycleRun() {
        world.washer.toggleCycleRun()
        updateWasherData()
    }

    /**
     * Finds the laundry item that should receive the selection after the given item is transferred.
     * **Must be called before the transfer is made.**
     */
    private fun findNextSelection(item: Body): Body? {
        val uiPotentialLaundry = uiState!!.laundryPanel.potentialLaundry
        val uiLoadedLaundry = uiState!!.laundryPanel.loadedLaundry
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
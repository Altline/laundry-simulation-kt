package altline.appliance.ui.mapper

import altline.appliance.common.Body
import altline.appliance.fabric.Clothing
import altline.appliance.substance.Soakable
import altline.appliance.ui.component.laundry.LaundryListItemUi
import altline.appliance.ui.component.laundry.LaundryPanelUi

class LaundryMapper(
    private val stringMapper: StringMapper
) {

    fun mapToLaundryPanel(
        potentialLaundry: Set<Body>,
        loadedLaundry: Set<Body>,
        selectedLaundryItem: Body?,
        onItemClick: (Body) -> Unit,
        onItemDoubleClick: (Body) -> Unit,
        onTransferClick: () -> Unit
    ): LaundryPanelUi {
        val bodyToListItem = { item: Body ->
            mapToLaundryListItem(
                item,
                selected = item == selectedLaundryItem,
                onClick = { onItemClick(item) },
                onDoubleClick = { onItemDoubleClick(item) }
            )
        }
        return LaundryPanelUi(
            potentialLaundry = potentialLaundry.map(bodyToListItem),
            loadedLaundry = loadedLaundry.map(bodyToListItem),
            onTransferClick = if (selectedLaundryItem != null) onTransferClick else null
        )
    }

    private fun mapToLaundryListItem(
        body: Body,
        selected: Boolean,
        onClick: () -> Unit,
        onDoubleClick: () -> Unit
    ): LaundryListItemUi {
        with(body) {
            val name = stringMapper.mapLaundryName(this)
            val size = if (this is Clothing) stringMapper.mapClothingSize(this.size) else ""
            val soakRatio = if (this is Soakable) this.soakRatio else null
            return LaundryListItemUi(
                id = body.id,
                title = "$name, $size",
                stainRatio = this.stainRatio,
                soakRatio = soakRatio,
                selected = selected,
                onClick = onClick,
                onDoubleClick = onDoubleClick
            )
        }
    }
}
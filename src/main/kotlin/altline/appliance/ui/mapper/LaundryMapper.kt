package altline.appliance.ui.mapper

import altline.appliance.common.Body
import altline.appliance.fabric.Clothing
import altline.appliance.fabric.Fabric
import altline.appliance.fabric.Shirt
import altline.appliance.substance.Soakable
import altline.appliance.ui.component.laundry.LaundryListItemUi
import altline.appliance.ui.component.laundry.LaundryPanelUi
import altline.appliance.ui.component.washer.InfoPanelUi
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import altline.appliance.washing.laundry.StandardLaundryWasherBase

class LaundryMapper {

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

    fun mapToInfoPanel(washer: StandardLaundryWasherBase): InfoPanelUi {
        return InfoPanelUi(
            washCycleName = washer.selectedWashCycle.name // TODO convert to resource
        )
    }

    private fun mapToLaundryListItem(
        body: Body,
        selected: Boolean,
        onClick: () -> Unit,
        onDoubleClick: () -> Unit
    ): LaundryListItemUi {
        with(body) {
            val name = mapToLaundryName(this)
            val size = if (this is Clothing) mapClothingSizeToString(this.size) else ""
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

    private fun mapToLaundryName(body: Body): String {
        return when (body) {
            is Shirt -> strings["laundryName_shirt"]
            is Clothing -> strings["laundryName_clothing"]
            is Fabric -> strings["laundryName_fabric"]
            else -> strings["laundryName_body"]
        }
    }

    private fun mapClothingSizeToString(size: Clothing.Size): String {
        return when (size) {
            Clothing.Size.XXS -> strings["clothingSize_XXS"]
            Clothing.Size.XS -> strings["clothingSize_XS"]
            Clothing.Size.S -> strings["clothingSize_S"]
            Clothing.Size.M -> strings["clothingSize_M"]
            Clothing.Size.L -> strings["clothingSize_L"]
            Clothing.Size.XL -> strings["clothingSize_XL"]
            Clothing.Size.XXL -> strings["clothingSize_XXL"]
        }
    }
}
package altline.appliance.ui.mapper

import altline.appliance.common.Body
import altline.appliance.fabric.Clothing
import altline.appliance.fabric.Fabric
import altline.appliance.fabric.Shirt
import altline.appliance.substance.Soakable
import altline.appliance.ui.component.LaundryListItemUi
import altline.appliance.ui.component.LaundryPanelUi
import altline.appliance.ui.resources.Strings

class LaundryMapper {

    fun mapToLaundryPanel(
        potentialLaundry: Set<Body>,
        loadedLaundry: Set<Body>
    ): LaundryPanelUi {
        return LaundryPanelUi(
            potentialLaundry = potentialLaundry.map(this::mapToLaundryListItem),
            loadedLaundry = loadedLaundry.map(this::mapToLaundryListItem)
        )
    }

    private fun mapToLaundryListItem(body: Body): LaundryListItemUi {
        with(body) {
            val name = mapToLaundryName(this)
            val size = if (this is Clothing) mapClothingSizeToString(this.size) else ""
            val soakRatio = if (this is Soakable) this.soakRatio else null
            return LaundryListItemUi(
                title = "$name, $size",
                stainRatio = this.stainRatio,
                soakRatio = soakRatio
            )
        }
    }

    private fun mapToLaundryName(body: Body): String {
        return when (body) {
            is Shirt -> Strings.laundryName_shirt
            is Clothing -> Strings.laundryName_clothing
            is Fabric -> Strings.laundryName_fabric
            else -> Strings.laundryName_body
        }
    }

    private fun mapClothingSizeToString(size: Clothing.Size): String {
        return when (size) {
            Clothing.Size.XXS -> Strings.clothingSize_XXS
            Clothing.Size.XS -> Strings.clothingSize_XS
            Clothing.Size.S -> Strings.clothingSize_S
            Clothing.Size.M -> Strings.clothingSize_M
            Clothing.Size.L -> Strings.clothingSize_L
            Clothing.Size.XL -> Strings.clothingSize_XL
            Clothing.Size.XXL -> Strings.clothingSize_XXL
        }
    }
}
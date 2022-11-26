package altline.appliance.ui.resources

import java.util.*

val strings: ResourceBundle by lazy { ResourceBundle.getBundle("Strings") }
operator fun ResourceBundle.get(key: String): String = getString(key)
private fun getString(key: String): String = strings[key]

object Strings {
    val clothingSize_XXS = getString("clothingSize_XXS")
    val clothingSize_XS = getString("clothingSize_XS")
    val clothingSize_S = getString("clothingSize_S")
    val clothingSize_M = getString("clothingSize_M")
    val clothingSize_L = getString("clothingSize_L")
    val clothingSize_XL = getString("clothingSize_XL")
    val clothingSize_XXL = getString("clothingSize_XXL")

    val laundryName_body = getString("laundryName_body")
    val laundryName_fabric = getString("laundryName_fabric")
    val laundryName_clothing = getString("laundryName_clothing")
    val laundryName_shirt = getString("laundryName_shirt")
}
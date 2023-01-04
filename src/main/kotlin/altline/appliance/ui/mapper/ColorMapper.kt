package altline.appliance.ui.mapper

import altline.appliance.measure.Volume.Companion.milliliters
import altline.appliance.substance.*
import altline.appliance.ui.theme.SubstanceColors
import altline.appliance.washing.CommonDetergents
import androidx.compose.ui.graphics.Color

class ColorMapper {

    fun mapSubstanceToColor(substance: Substance): Color? = synchronized(substance) {
        return if (substance.isNotEmpty()) {
            var red = 0f
            var green = 0f
            var blue = 0f
            var totalUnits = 0f
            substance.parts.forEach { part ->
                val color = mapSubstanceTypeToColor(part.type)
                val amountUnits = (part.amount `in` milliliters).toFloat()
                red += color.red * amountUnits
                green += color.green * amountUnits
                blue += color.blue * amountUnits
                totalUnits += amountUnits
            }
            return Color(red / totalUnits, green / totalUnits, blue / totalUnits)
        } else null
    }

    fun mapSubstanceTypeToColor(substanceType: SubstanceType): Color {
        return when (substanceType) {
            CommonSubstanceTypes.WATER -> SubstanceColors.Water
            CommonSubstanceTypes.COFFEE -> SubstanceColors.Coffee

            CommonDetergents.ULTIMATE_DETERGENT -> SubstanceColors.UltimateDetergent
            CommonDetergents.STRONG_DETERGENT -> SubstanceColors.StrongDetergent
            CommonDetergents.BASIC_DETERGENT -> SubstanceColors.BasicDetergent
            CommonDetergents.MILD_DETERGENT -> SubstanceColors.MildDetergent
            CommonDetergents.WEAK_DETERGENT -> SubstanceColors.WeakDetergent
            CommonDetergents.BARELY_DETERGENT -> SubstanceColors.BarelyDetergent
            CommonDetergents.USELESS_DETERGENT -> SubstanceColors.UselessDetergent

            CommonFabricSofteners.ULTIMATE_SOFTENER -> SubstanceColors.UltimateSoftener
            CommonFabricSofteners.STRONG_SOFTENER -> SubstanceColors.StrongSoftener
            CommonFabricSofteners.BASIC_SOFTENER -> SubstanceColors.BasicSoftener
            CommonFabricSofteners.MILD_SOFTENER -> SubstanceColors.MildSoftener
            CommonFabricSofteners.WEAK_SOFTENER -> SubstanceColors.WeakSoftener
            CommonFabricSofteners.BARELY_SOFTENER -> SubstanceColors.BarelySoftener
            CommonFabricSofteners.USELESS_SOFTENER -> SubstanceColors.UselessSoftener

            else -> Color.Transparent
        }
    }
}
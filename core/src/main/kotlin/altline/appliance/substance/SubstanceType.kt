package altline.appliance.substance

interface SubstanceType {
    val evaporates: Boolean
}

enum class CommonSubstanceTypes(
    override val evaporates: Boolean = false
) : SubstanceType {
    WATER(evaporates = true), COFFEE
}
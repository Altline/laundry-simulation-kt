package altline.appliance.substance

interface SubstanceType {
    val evaporates: Boolean
    val consistency: SubstanceConsistency
}

enum class CommonSubstanceTypes(
    override val consistency: SubstanceConsistency,
    override val evaporates: Boolean = false
) : SubstanceType {
    WATER(SubstanceConsistency.ThinLiquid, evaporates = true),
    MUD(SubstanceConsistency.ThickLiquid),
    COFFEE(SubstanceConsistency.ThinLiquid),
    KETCHUP(SubstanceConsistency.ThickLiquid),
    CRUDE_OIL(SubstanceConsistency.ThickLiquid)
}
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
    COFFEE(SubstanceConsistency.ThinLiquid)
}
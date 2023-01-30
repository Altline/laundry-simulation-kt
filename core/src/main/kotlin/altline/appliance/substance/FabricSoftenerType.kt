package altline.appliance.substance

interface FabricSoftenerType : SubstanceType {
    val fresheningPotential: Double
}

enum class CommonFabricSofteners(
    override val fresheningPotential: Double,
    override val consistency: SubstanceConsistency = SubstanceConsistency.ThickLiquid
) : FabricSoftenerType {

    ULTIMATE_SOFTENER(1.0),
    STRONG_SOFTENER(0.95),
    BASIC_SOFTENER(0.8),
    MILD_SOFTENER(0.6),
    WEAK_SOFTENER(0.4),
    BARELY_SOFTENER(0.2),
    USELESS_SOFTENER(0.0);

    override val evaporates: Boolean = false
}
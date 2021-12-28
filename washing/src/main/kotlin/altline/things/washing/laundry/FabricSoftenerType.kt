package altline.things.washing.laundry

import altline.things.substances.SubstanceType

interface FabricSoftenerType : SubstanceType {
    val freshness: Double
}

enum class CommonFabricSofteners(
    override val freshness: Double
) : FabricSoftenerType {

    USELESS_SOFTENER(0.0),
    BARELY_SOFTENER(0.2),
    WEAK_SOFTENER(0.4),
    MILD_SOFTENER(0.6),
    BASIC_SOFTENER(0.8),
    STRONG_SOFTENER(0.95),
    ULTIMATE_SOFTENER(1.0)
}
package altline.things.substances

interface SubstanceType {
    /** The minimum ratio of this substance to all substances in the mix where the properties of this substance type
     * are still at their nominal value. */
    val nominalSaturation: Double
}

enum class CommonSubstanceTypes(
    override val nominalSaturation: Double
) : SubstanceType {
    WATER(0.0)
}
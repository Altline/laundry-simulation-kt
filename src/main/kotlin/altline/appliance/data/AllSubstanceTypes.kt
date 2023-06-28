package altline.appliance.data

import altline.appliance.substance.CommonSubstanceTypes
import altline.appliance.substance.SubstanceType
import altline.appliance.washing.CommonDetergents
import altline.appliance.washing.laundry.CommonFabricSofteners

val AllSubstanceTypes = buildList<SubstanceType> {
    addAll(CommonSubstanceTypes.values())
    addAll(CommonDetergents.values())
    addAll(CommonFabricSofteners.values())
}
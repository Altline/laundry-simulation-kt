package altline.appliance.measure

import altline.appliance.measure.Energy.Companion.joules
import altline.appliance.measure.Energy.Companion.kilojoules
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

typealias VolumetricFlow = UnitsRatio<Volume, Time>
typealias Power = UnitsRatio<Energy, Time>

val watts = joules / seconds
val kilowatts = kilojoules / seconds
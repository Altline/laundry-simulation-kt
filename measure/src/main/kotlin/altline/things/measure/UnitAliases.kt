package altline.things.measure

import io.nacular.measured.units.*

typealias VolumetricFlow = UnitsRatio<Volume, Time>
typealias Power = UnitsRatio<Energy, Time>

val watts = Energy.joules / Time.seconds
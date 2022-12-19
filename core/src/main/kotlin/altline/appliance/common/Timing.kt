package altline.appliance.common

import altline.appliance.measure.toFrequency
import altline.appliance.measure.toPeriod
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

var RefreshPeriod = 0.5 * seconds
var RefreshRate
    get() = RefreshPeriod.toFrequency()
    set(value) {
        RefreshPeriod = value.toPeriod()
    }

var SpeedModifier = 1.0

val TimeFactor get() = RefreshPeriod * SpeedModifier
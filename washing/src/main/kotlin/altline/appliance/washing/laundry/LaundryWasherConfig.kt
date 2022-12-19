package altline.appliance.washing.laundry

import altline.appliance.common.SpeedModifier
import altline.appliance.measure.Frequency
import altline.appliance.measure.Frequency.Companion.hertz
import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.measure.VolumetricFlow
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

data class LaundryWasherConfig(
    val waterMeasureRate: Measure<Frequency> = (1.0 * hertz) * SpeedModifier,

    val intakeFlowRate: Measure<VolumetricFlow> = 0.2 * liters / seconds,
    val outputFlowRate: Measure<VolumetricFlow> = 1.0 * liters / seconds,

    val centrifugeThreshold: Measure<Spin> = 80 * rpm,

    /** The lower soak ratio limit where washing can take place */
    val lowerSoakRatio: Double = 0.75,

    /** The upper soak ratio limit where washing effectiveness no longer depends on soakness. */
    val upperSoakRatio: Double = 1.25,

    val nominalResoakFactor: Double = 0.1,
)

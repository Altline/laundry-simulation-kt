package altline.things.washing.laundry

import altline.things.measure.Frequency
import altline.things.measure.Frequency.Companion.hertz
import altline.things.measure.Spin
import altline.things.measure.Spin.Companion.rpm
import altline.things.measure.Volume
import altline.things.measure.Volume.Companion.liters
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

data class LaundryWasherConfig(
    val waterMeasureRate: Measure<Frequency> = 1.0 * hertz,

    val intakeFlowRate: Measure<UnitsRatio<Volume, Time>> = 0.2 * liters / seconds,
    val outputFlowRate: Measure<UnitsRatio<Volume, Time>> = 1.0 * liters / seconds,

    val centrifugeThreshold: Measure<Spin> = 80 * rpm,

    /** The lower soak ratio limit where washing can take place */
    val lowerSoakRatio: Double = 0.75,

    /** The upper soak ratio limit where washing effectiveness no longer depends on soakness. */
    val upperSoakRatio: Double = 1.25,

    val NominalResoakFactor: Double = 0.1,
)

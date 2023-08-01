package altline.appliance.washing.laundry

import altline.appliance.measure.Volume.Companion.liters
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.seconds

/**
 * Reference constant for a laundry washer's average liquid intake flow rate.
 */
val StandardIntakeFlowRate = (0.15 * liters / seconds)

/**
 * Reference constant for a laundry washer's average liquid output flow rate.
 */
val StandardOutputFlowRate = (0.35 * liters / seconds)

/**
 * Reference constant for an estimated time it takes a laundry washer to drain a loaded drum.
 * Used for estimations.
 */
val StandardFullFlowDrainTime = 30 * seconds
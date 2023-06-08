package altline.appliance.measure

import io.nacular.measured.units.*

class MeasuringTrigger<T : Units>(
    var triggerSetting: Measure<T>,
    var tolerance: Measure<T>,
    var onDropBelow: (() -> Unit)? = null,
    var onRiseAbove: (() -> Unit)? = null
) {
    fun check(measure: Measure<T>) {
        when {
            measure < triggerSetting - tolerance -> onDropBelow?.invoke()
            measure > triggerSetting + tolerance -> onRiseAbove?.invoke()
        }
    }
}
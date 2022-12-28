package altline.appliance.measure

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Units

class MeasuringTrigger<T : Units>(
    var triggerSetting: Measure<T>,
    var tolerance: Measure<T>,
    var onDropBelow: () -> Unit,
    var onRiseAbove: () -> Unit
) {
    fun check(measure: Measure<T>) {
        when {
            measure < triggerSetting - tolerance -> onDropBelow()
            measure > triggerSetting + tolerance -> onRiseAbove()
        }
    }
}
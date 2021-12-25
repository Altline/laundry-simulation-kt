package altline.things.measure

import altline.things.util.isNegligible
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.Units

suspend fun delay(time: Measure<Time>) = kotlinx.coroutines.delay((time `in` Time.milliseconds).toLong())

fun <T : Units> Measure<T>.isNegligible(threshold: Double = 0.00001): Boolean
        = amount.isNegligible(threshold)
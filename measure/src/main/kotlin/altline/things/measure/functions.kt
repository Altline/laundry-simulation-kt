package altline.things.measure

import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time

suspend fun delay(time: Measure<Time>) = kotlinx.coroutines.delay((time `in` Time.milliseconds).toLong())
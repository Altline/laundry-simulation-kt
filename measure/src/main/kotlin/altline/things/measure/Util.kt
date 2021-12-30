package altline.things.measure

import altline.things.measure.Frequency.Companion.hertz
import altline.things.util.isNegligible
import io.nacular.measured.units.Measure
import io.nacular.measured.units.Time
import io.nacular.measured.units.Time.Companion.milliseconds
import io.nacular.measured.units.Time.Companion.seconds
import io.nacular.measured.units.Units
import io.nacular.measured.units.times

fun Measure<Frequency>.toPeriod() = (1 / (this `in` hertz)) * seconds
fun Measure<Time>.toFrequency() = (1 / (this `in` seconds)) * hertz

suspend fun delay(time: Measure<Time>) = kotlinx.coroutines.delay((time `in` milliseconds).toLong())

@JvmName("repeatPeriodically1")
suspend fun repeatPeriodically(frequency: Measure<Frequency>, times: Int = -1, action: (Long) -> Unit) =
    repeatPeriodically(frequency.toPeriod(), times, action)

suspend fun repeatPeriodically(period: Measure<Time>, times: Int = -1, action: (Long) -> Unit) {
    if (times < 0) {
        var count = 0L
        while (true) {
            action(count++)
            delay(period)
        }
    } else {
        repeat(times) {
            action(it.toLong())
            delay(period)
        }
    }
}

fun <T : Units> Measure<T>.isNegligible(threshold: Double = 0.00001): Boolean
        = amount.isNegligible(threshold)
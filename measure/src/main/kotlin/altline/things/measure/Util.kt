package altline.things.measure

import altline.things.measure.Frequency.Companion.hertz
import altline.things.util.closeEquals
import altline.things.util.isNegligible
import altline.things.util.isNotNegligible
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.milliseconds
import io.nacular.measured.units.Time.Companion.seconds

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

fun <T : Units> Measure<T>.isNegligible(threshold: Double = 0.00001) = amount.isNegligible(threshold)
fun <T : Units> Measure<T>.isNotNegligible(threshold: Double = 0.00001) = amount.isNotNegligible(threshold)
fun <T : Units> Measure<T>.closeEquals(other: Measure<T>, threshold: Double = 0.00001): Boolean {
    val resultUnit = minOf(units, other.units)
    val a = this  `in` resultUnit
    val b = other `in` resultUnit
    return a.closeEquals(b, threshold)
}

/** Redefinition of an existing div function from [Units] to avoid overload ambiguity. */
fun <T : Units> Measure<T>.divSameUnit(other: Measure<T>): Double =
    amount / other.amount * (units.ratio / other.units.ratio)
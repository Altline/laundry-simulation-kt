package altline.things.measure

import altline.things.measure.Energy.Companion.joules
import altline.things.measure.Frequency.Companion.hertz
import altline.things.measure.Power.Companion.watts
import altline.things.measure.Spin.Companion.rpm
import altline.things.measure.Volume.Companion.liters
import altline.things.util.closeEquals
import altline.things.util.isNegligible
import altline.things.util.isNotNegligible
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.milliseconds
import io.nacular.measured.units.Time.Companion.seconds

val SUnits: Units? = null
inline fun <reified T : Units> Units?.zero(): Measure<T> {
    return when (T::class) {
        Energy::class -> 0.0 * (joules as T)
        Power::class -> 0.0 * (watts as T)
        Frequency::class -> 0.0 * (hertz as T)
        Time::class -> 0.0 * (seconds as T)
        Spin::class -> 0.0 * (rpm as T)
        Volume::class -> 0.0 * (liters as T)
        else -> throw RuntimeException()
    }
}

fun Measure<Frequency>.toPeriod() = (1 / (this `in` hertz)) * seconds
fun Measure<Time>.toFrequency() = (1 / (this `in` seconds)) * hertz

suspend fun delay(time: Measure<Time>) = kotlinx.coroutines.delay((time `in` milliseconds).toLong())

@JvmName("repeatPeriodically1")
suspend fun repeatPeriodically(frequency: Measure<Frequency>, times: Int = -1, action: (Long) -> Unit) =
    repeatPeriodically(frequency.toPeriod(), times, action)

suspend fun repeatPeriodically(period: Measure<Time>, times: Int = -1, action: suspend (Long) -> Unit) {
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
    val a = this `in` resultUnit
    val b = other `in` resultUnit
    return a.closeEquals(b, threshold)
}

inline fun <T, reified U : Units> Collection<T>.sumOf(selector: (T) -> Measure<U>): Measure<U> {
    var sum: Measure<U> = SUnits.zero()
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

/** Redefinition of an existing div function from [Units] to avoid overload ambiguity. */
fun <T : Units> Measure<T>.divSameUnit(other: Measure<T>): Double =
    amount / other.amount * (units.ratio / other.units.ratio)
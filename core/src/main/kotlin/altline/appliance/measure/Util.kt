package altline.appliance.measure

import altline.appliance.measure.Energy.Companion.joules
import altline.appliance.measure.Frequency.Companion.hertz
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.measure.Temperature.Companion.celsius
import altline.appliance.measure.Volume.Companion.liters
import altline.appliance.util.closeEquals
import altline.appliance.util.isNegligible
import altline.appliance.util.isNotNegligible
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.hours
import io.nacular.measured.units.Time.Companion.milliseconds
import io.nacular.measured.units.Time.Companion.minutes
import io.nacular.measured.units.Time.Companion.seconds
import mu.KotlinLogging
import java.time.LocalTime
import kotlin.reflect.typeOf

/**
 * Returns a measure with a value of 0 and a unit determined from the reified generic type. If the type could not be
 * determined, for example, when a nested type is generic (`UnitsRatio<QuantityType, Time>`), an [UnsupportedZeroException] is thrown.
 */
inline fun <reified T : Units> zero(): Measure<T> {
    return when (typeOf<T>()) {
        typeOf<Energy>() -> 0.0 * (joules as T)
        typeOf<Frequency>() -> 0.0 * (hertz as T)
        typeOf<Time>() -> 0.0 * (seconds as T)
        typeOf<Spin>() -> 0.0 * (rpm as T)
        typeOf<Volume>() -> 0.0 * (liters as T)
        typeOf<Temperature>() -> 0.0 * (celsius as T)
        typeOf<Power>() -> 0.0 * (watts as T)
        typeOf<VolumetricFlow>() -> 0.0 * ((liters / seconds) as T)
        else -> throw UnsupportedZeroException()
    }
}

/**
 * Signifies that a generic measurement unit type could not be determined at runtime.
 */
class UnsupportedZeroException : Exception()

fun Measure<Frequency>.toPeriod() = (1 / (this `in` hertz)) * seconds
fun Measure<Time>.toFrequency() = (1 / (this `in` seconds)) * hertz

fun Measure<Time>.toLocalTime(): LocalTime {
    val hour = (this `in` hours).toInt().also {
        if (it > 23) {
            KotlinLogging.logger("Util")
                .warn("Time measure exceeds 23 hours. The resulting LocalTime hour field will be capped at 23!")
        }
    }
    return LocalTime.of(
        hour % 24,
        (this `in` minutes).toInt() % 60,
        (this `in` seconds).toInt() % 60
    )
}

suspend fun delay(time: Measure<Time>) = kotlinx.coroutines.delay((time `in` milliseconds).toLong())

@JvmName("repeatPeriodically1")
suspend inline fun repeatPeriodically(frequency: Measure<Frequency>, times: Int = -1, action: (Long) -> Unit) =
    repeatPeriodically(frequency.toPeriod(), times, action)

suspend inline fun repeatPeriodically(period: Measure<Time>, times: Int = -1, action: (Long) -> Unit) {
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

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 *
 * *Note*: Due to the current flaw in determining the exact type of generic measurement unit in some situations,
 * this function may throw an [UnsupportedZeroException] if this collection is empty and the type wasn't determined.
 */
inline fun <T, reified U : Units> Iterable<T>.sumOf(selector: (T) -> Measure<U>): Measure<U> =
    fold(zero<U>()) { acc, value -> acc + selector(value) }

/**
 * @see Iterable.sumOf
 */
inline fun <T, reified U : Units> Array<T>.sumOf(selector: (T) -> Measure<U>): Measure<U> =
    asIterable().sumOf(selector)

/**
 * Returns the sum of all values produced by [selector] function applied to each element in the collection.
 * Returns `null` if the collection is empty.
 */
fun <T, U : Units> Iterable<T>.sumOfOrNull(selector: (T) -> Measure<U>): Measure<U>? =
    map(selector).reduceOrNull { a, b -> a + b }

/**
 * @see Iterable.sumOfOrNull
 */
fun <T, U : Units> Array<T>.sumOfOrNull(selector: (T) -> Measure<U>): Measure<U>? =
    asIterable().sumOfOrNull(selector)

/** Redefinition of an existing div function from [Units] to avoid overload ambiguity. */
fun <T : Units> Measure<T>.divSameUnit(other: Measure<T>): Double =
    amount / other.amount * (units.ratio / other.units.ratio)
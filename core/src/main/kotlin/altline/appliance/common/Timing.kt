package altline.appliance.common

import altline.appliance.measure.toFrequency
import altline.appliance.measure.toPeriod
import io.nacular.measured.units.*
import io.nacular.measured.units.Time.Companion.milliseconds
import io.nacular.measured.units.Time.Companion.seconds
import kotlinx.coroutines.delay

var RefreshPeriod = 0.5 * seconds
var RefreshRate
    get() = RefreshPeriod.toFrequency()
    set(value) {
        RefreshPeriod = value.toPeriod()
    }

var SpeedModifier: Float = 1f

val TimeFactor get() = RefreshPeriod * SpeedModifier

/**
 * A rough mimic of [delay] that can takes [SpeedModifier] into account.
 */
suspend fun delaySpeedAware(time: Measure<Time>, checkInterval: Measure<Time> = 1 * seconds) {
    val actualTime = time / SpeedModifier
    val checkIntervalMillis = (checkInterval `in` milliseconds).toLong()
    val preciseNumberOfChecks = actualTime / checkInterval
    val wholeNumberOfChecks = preciseNumberOfChecks.toInt()
    val remainingTime = (checkIntervalMillis * (preciseNumberOfChecks - wholeNumberOfChecks)).toLong()

    repeat(wholeNumberOfChecks) { i ->
        val prevSpeedModifier = SpeedModifier
        delay(checkIntervalMillis)
        if (SpeedModifier != prevSpeedModifier) {
            val newTime = actualTime - (i + 1) * checkInterval
            delaySpeedAware(newTime, checkInterval)
            return
        }
    }
    delay(remainingTime)
}
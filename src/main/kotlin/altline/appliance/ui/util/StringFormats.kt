package altline.appliance.ui.util

import altline.appliance.measure.toLocalTime
import io.nacular.measured.units.*
import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

private val percentFormat = DecimalFormat.getPercentInstance().apply {
    maximumFractionDigits = 2
}

private val optionalDecimalFormat = DecimalFormat.getInstance().apply {
    isGroupingUsed = false
    minimumFractionDigits = 0
}

private val clockTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss")

fun formatPercentage(number: Double): String {
    return percentFormat.format(number)
}

fun Measure<*>.optionalDecimal(): String {
    return "${optionalDecimalFormat.format(amount)} ${units.suffix}"
}

fun Measure<Time>.clockFormat(): String {
    return toLocalTime().format(clockTimeFormat)
}

fun Measure<Time>.unitFormat(minimized: Boolean = true): String {
    val rawSeconds = (this `in` Time.seconds).roundToInt()
    val rawMinutes = rawSeconds / 60
    val hours = rawMinutes / 60
    val minutes = rawMinutes % 60
    val seconds = rawSeconds % 60

    return if (minimized) {
        buildString {
            if (hours != 0) append("${hours}h ")
            if (minutes != 0) append("${minutes}min ")
            append("${seconds}s")
        }
    } else {
        "${hours}h ${minutes}min ${seconds}s"
    }
}

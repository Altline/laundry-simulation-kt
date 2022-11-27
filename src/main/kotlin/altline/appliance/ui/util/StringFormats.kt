package altline.appliance.ui.util

import java.text.DecimalFormat

private val percentFormat = DecimalFormat.getPercentInstance().apply {
    maximumFractionDigits = 2
}

fun formatPercentage(number: Double): String {
    return percentFormat.format(number)
}
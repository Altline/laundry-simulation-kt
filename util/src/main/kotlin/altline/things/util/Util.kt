package altline.things.util

fun Double.isNegligible(threshold: Double): Boolean
        = this >= -threshold && this <= threshold
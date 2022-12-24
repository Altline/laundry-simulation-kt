package altline.appliance.ui.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.lerp

fun lerpCoerced(start: Dp, stop: Dp, fraction: Float): Dp {
    val coercedFraction = fraction.coerceIn(0f, 1f)
    return lerp(start, stop, coercedFraction)
}
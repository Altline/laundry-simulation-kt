package altline.appliance.ui.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*
import kotlin.math.absoluteValue

@Composable
fun animateRpmAsState(rpm: Float): State<Float> {
    var prevRpm by remember { mutableStateOf(0f) }
    return animateFloatAsState(
        targetValue = rpm,
        animationSpec = tween(((rpm - prevRpm).absoluteValue * 20).toInt(), easing = LinearEasing)
    ).also {
        if (prevRpm != rpm) prevRpm = rpm
    }
}
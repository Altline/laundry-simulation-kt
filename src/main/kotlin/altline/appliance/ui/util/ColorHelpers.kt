package altline.appliance.ui.util

import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.graphics.Color

@Composable
fun modifiedColor(
    color: Color = LocalContentColor.current,
    alpha: Float = LocalContentAlpha.current
): Color {
    return color.copy(alpha = alpha)
}

@Composable
fun ProvideContentColor(
    color: Color? = null,
    alpha: Float? = null,
    content: @Composable () -> Unit
) {
    val providedValues = buildList<ProvidedValue<*>> {
        if (color != null) add(LocalContentColor provides color)
        if (alpha != null) add(LocalContentAlpha provides alpha)
    }

    if (providedValues.isNotEmpty()) {
        CompositionLocalProvider(*providedValues.toTypedArray(), content = content)
    } else {
        content()
    }
}
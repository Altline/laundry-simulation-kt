package altline.appliance.ui.component.washer

import altline.appliance.measure.Spin
import altline.appliance.measure.Spin.Companion.rpm
import altline.appliance.ui.theme.AppTheme
import altline.appliance.ui.util.lerpCoerced
import altline.appliance.util.lerpCoerced
import androidx.compose.animation.core.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.nacular.measured.units.Measure
import io.nacular.measured.units.times

@Composable
fun Drum(data: DrumUi) {
    var rotation by remember { mutableStateOf(0f) }
    val infiniteTransition = rememberInfiniteTransition()
    val tick by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 60000,
                easing = LinearEasing
            )
        )
    )

    val rpm = data.spinSpeed `in` rpm

    val fastRpmThreshold = 100
    val spinBlur = lerpCoerced(0.dp, 8.dp, ((rpm - fastRpmThreshold) / 1000f).toFloat())
    val spinAlpha = lerpCoerced(255f, 100f, ((rpm - 200f) / 1500f).toFloat())
    val spinBackgroundAlpha = lerpCoerced(0f, 1f, ((rpm - fastRpmThreshold) / 1000f).toFloat())

    val drumOffsetY = (-75).dp
    val glassDiameter = 320.dp

    LaunchedEffect(tick) {
        rotation += (0.1f * rpm).toFloat()
    }

    Surface(
        Modifier
            .offset(y = drumOffsetY)
            .size(520.dp),
        shape = CircleShape,
        border = BorderStroke(2.dp, Color.LightGray)
    ) {}
    Surface(
        Modifier
            .offset(y = drumOffsetY)
            .size(460.dp),
        shape = CircleShape,
        color = Color(0xfff7f7f7),
        border = BorderStroke(1.dp, Color(0xffe0e0e0))
    ) {}
    Box(
        Modifier
            .offset(y = drumOffsetY)
            .size(glassDiameter)
            .clip(CircleShape)
            .border(2.dp, Color.LightGray, CircleShape)
            .blur(spinBlur)
            .background(Color(0xfffdfdfd))
            .graphicsLayer { rotationZ = rotation },
        contentAlignment = Alignment.Center
    ) {
        if (rpm > fastRpmThreshold) {
            Image(
                painterResource("images/DrumBlur.png"),
                contentDescription = null,
                Modifier
                    .fillMaxSize()
                    .blur(spinBlur)
                    .graphicsLayer {
                        alpha = spinBackgroundAlpha
                        rotationZ = rotation / 3
                    }
            )
        }

        val drumFeatureColor = Color(
            245, 245, 245,
            alpha = spinAlpha.toInt()
        )
        for (i in 0..2) {
            Surface(
                Modifier
                    .rotate(i * 360f / 3)
                    .offset(y = -glassDiameter / 4)
                    .size(width = 40.dp, height = glassDiameter / 2),
                color = drumFeatureColor,
                border = BorderStroke(1.dp, Color.LightGray)
            ) {}
        }
        Surface(
            Modifier.size(80.dp),
            shape = CircleShape,
            color = drumFeatureColor,
            border = BorderStroke(1.dp, Color.LightGray)
        ) {}
    }
}

data class DrumUi(
    val spinSpeed: Measure<Spin>
) {
    companion object {
        @Composable
        fun preview() = DrumUi(
            spinSpeed = 60 * rpm
        )
    }
}

@Preview
@Composable
private fun PreviewDrum() {
    AppTheme {
        Drum(DrumUi.preview())
    }
}
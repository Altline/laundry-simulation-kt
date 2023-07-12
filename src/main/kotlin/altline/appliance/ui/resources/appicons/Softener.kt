package altline.appliance.ui.resources.appicons

import altline.appliance.ui.resources.AppIcons
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val AppIcons.Softener: ImageVector
    get() {
        if (_softener != null) {
            return _softener!!
        }
        _softener = Builder(name = "Softener", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveToRelative(10.1367f, 0.2525f)
                curveToRelative(-0.9984f, 0.2853f, -2.3296f, 1.6164f, -2.6624f, 2.6386f)
                lineToRelative(-0.2615f, 0.8082f)
                lineToRelative(-1.4976f, -0.0713f)
                curveToRelative(-1.8066f, -0.1189f, -2.9952f, 0.4754f, -4.0649f, 1.9968f)
                curveToRelative(-1.2123f, 1.7353f, -1.1172f, 4.5403f, 0.2139f, 5.7764f)
                lineToRelative(0.5705f, 0.523f)
                lineToRelative(-0.7607f, 0.9508f)
                curveToRelative(-0.6181f, 0.7607f, -0.7607f, 1.1886f, -0.832f, 2.4247f)
                curveToRelative(-0.0713f, 1.0935f, 0.0f, 1.7115f, 0.309f, 2.3058f)
                curveToRelative(1.1172f, 2.2107f, 3.114f, 3.3042f, 5.1821f, 2.8525f)
                curveToRelative(0.832f, -0.1902f, 0.8795f, -0.1664f, 1.1172f, 0.5943f)
                curveToRelative(0.6418f, 1.8779f, 2.2345f, 2.9476f, 4.4452f, 2.9476f)
                curveToRelative(2.3296f, -0.0238f, 3.7321f, -0.9271f, 4.5641f, -2.9952f)
                curveToRelative(0.2853f, -0.7131f, 0.3328f, -0.7369f, 1.1648f, -0.5467f)
                curveToRelative(2.6386f, 0.5943f, 5.2534f, -1.5689f, 5.4674f, -4.4928f)
                curveToRelative(0.0951f, -1.45f, -0.4041f, -2.9001f, -1.2361f, -3.5895f)
                curveToRelative(-0.3328f, -0.2615f, -0.2853f, -0.4279f, 0.3803f, -1.2361f)
                curveToRelative(1.0697f, -1.3312f, 1.2123f, -3.2091f, 0.3328f, -4.9206f)
                curveToRelative(-0.3328f, -0.6656f, -0.9984f, -1.4976f, -1.4976f, -1.8779f)
                curveToRelative(-0.7607f, -0.5943f, -1.0935f, -0.6656f, -2.6386f, -0.6894f)
                lineToRelative(-1.7591f, -0.0238f)
                lineToRelative(-0.4279f, -0.9271f)
                curveToRelative(-0.9984f, -2.2107f, -3.4944f, -3.2091f, -6.1092f, -2.4484f)
                close()
                moveTo(13.845f, 1.6075f)
                curveToRelative(0.8795f, 0.4754f, 1.6878f, 1.7115f, 1.6878f, 2.5673f)
                curveToRelative(0.0f, 0.5467f, -3.3042f, 6.5133f, -3.5895f, 6.5133f)
                curveToRelative(-0.3566f, -0.0f, -3.5419f, -5.8953f, -3.5419f, -6.5133f)
                curveToRelative(0.0f, -2.3534f, 3.114f, -3.8034f, 5.4436f, -2.5673f)
                close()
                moveTo(6.3095f, 4.7453f)
                curveToRelative(0.7845f, 0.2377f, 1.0222f, 0.523f, 2.6624f, 3.3517f)
                curveToRelative(0.9984f, 1.7115f, 1.8066f, 3.1616f, 1.8066f, 3.2091f)
                curveToRelative(0.0f, 0.0475f, -1.664f, 0.0951f, -3.6845f, 0.0951f)
                curveToRelative(-3.5895f, -0.0f, -3.7321f, -0.0238f, -4.2788f, -0.5705f)
                curveToRelative(-2.0206f, -2.0206f, -0.5467f, -5.7764f, 2.496f, -6.2994f)
                curveToRelative(0.0713f, -0.0238f, 0.4992f, 0.0951f, 0.9984f, 0.2139f)
                close()
                moveTo(20.287f, 5.197f)
                curveToRelative(2.0206f, 1.4263f, 2.282f, 4.4928f, 0.4754f, 5.8953f)
                curveToRelative(-0.4041f, 0.3328f, -7.6068f, 0.4517f, -7.583f, 0.1189f)
                curveToRelative(0.0f, -0.0951f, 0.7845f, -1.4976f, 1.7115f, -3.1378f)
                curveToRelative(1.4976f, -2.5673f, 1.8542f, -3.0189f, 2.5435f, -3.2567f)
                curveToRelative(0.9746f, -0.3328f, 2.0206f, -0.1902f, 2.8525f, 0.3803f)
                close()
                moveTo(10.7785f, 12.7087f)
                curveTo(10.7785f, 12.8751f, 7.5219f, 18.5326f, 7.2128f, 18.8892f)
                curveTo(6.785f, 19.3884f, 5.4538f, 19.5786f, 4.5029f, 19.2458f)
                curveTo(1.9356f, 18.4138f, 1.0561f, 15.1571f, 2.8627f, 13.2554f)
                lineToRelative(0.6418f, -0.6656f)
                lineToRelative(3.637f, -0.0f)
                curveToRelative(1.9968f, -0.0f, 3.637f, 0.0475f, 3.637f, 0.1189f)
                close()
                moveTo(21.1428f, 13.398f)
                curveToRelative(0.7131f, 0.7131f, 0.8082f, 0.9508f, 0.8082f, 2.1156f)
                curveToRelative(0.0f, 1.4263f, -0.5467f, 2.4722f, -1.7353f, 3.3517f)
                curveToRelative(-0.7845f, 0.5943f, -2.7337f, 0.6894f, -3.328f, 0.1664f)
                curveToRelative(-0.3803f, -0.309f, -3.7321f, -5.9666f, -3.7321f, -6.2994f)
                curveToRelative(0.0f, -0.0713f, 1.6164f, -0.1426f, 3.5895f, -0.1426f)
                lineTo(20.3346f, 12.5898f)
                close()
                moveTo(13.8212f, 16.2981f)
                curveToRelative(0.9508f, 1.664f, 1.7115f, 3.2804f, 1.7115f, 3.6132f)
                curveToRelative(0.0f, 1.4263f, -1.3074f, 2.6862f, -3.1378f, 3.0189f)
                curveToRelative(-1.8066f, 0.3328f, -3.9936f, -1.3312f, -3.9936f, -3.0427f)
                curveToRelative(0.0f, -0.6181f, 3.2091f, -6.5846f, 3.5419f, -6.5846f)
                curveToRelative(0.0713f, -0.0f, 0.9271f, 1.355f, 1.8779f, 2.9952f)
                close()
            }
        }
        .build()
        return _softener!!
    }

private var _softener: ImageVector? = null

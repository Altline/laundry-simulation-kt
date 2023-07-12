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

public val AppIcons.Detergent: ImageVector
    get() {
        if (_detergent != null) {
            return _detergent!!
        }
        _detergent = Builder(name = "Detergent", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.210777f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveToRelative(0.28f, 4.5345f)
                curveToRelative(-0.274f, 0.0632f, -0.3372f, 0.1897f, -0.2319f, 0.5269f)
                curveToRelative(0.0843f, 0.2529f, 1.1804f, 3.7308f, 2.4029f, 7.7355f)
                lineToRelative(2.2764f, 7.2718f)
                lineToRelative(7.2718f, 0.0632f)
                curveToRelative(6.787f, 0.0422f, 7.314f, 0.0211f, 7.4826f, -0.3162f)
                curveToRelative(0.1054f, -0.2108f, 1.2225f, -3.6886f, 2.4661f, -7.7145f)
                curveToRelative(1.6441f, -5.3959f, 2.1921f, -7.4193f, 2.0235f, -7.588f)
                curveToRelative(-0.1475f, -0.1475f, -0.2951f, -0.1475f, -0.4216f, -0.0211f)
                curveToRelative(-0.1475f, 0.1476f, -3.4567f, 10.6021f, -4.5739f, 14.4804f)
                curveToRelative(-0.1054f, 0.3372f, -0.5691f, 0.3583f, -6.8924f, 0.3162f)
                lineToRelative(-6.7659f, -0.0632f)
                lineToRelative(-2.2342f, -7.1664f)
                curveToRelative(-1.2225f, -3.9415f, -2.2764f, -7.2718f, -2.3185f, -7.3772f)
                curveToRelative(-0.0632f, -0.1265f, -0.274f, -0.1897f, -0.4848f, -0.1476f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.210777f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveToRelative(9.8282f, 6.4526f)
                curveToRelative(-0.1265f, 0.2529f, -0.2108f, 2.4661f, -0.2108f, 5.712f)
                curveToRelative(0.0f, 5.3959f, 0.0843f, 6.1125f, 0.801f, 6.1125f)
                curveToRelative(0.8431f, -0.0f, 0.8853f, -0.3162f, 0.8853f, -6.0282f)
                curveToRelative(0.0f, -3.0352f, -0.0632f, -5.6699f, -0.1265f, -5.8596f)
                curveToRelative(-0.1897f, -0.4637f, -1.075f, -0.4215f, -1.349f, 0.0632f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                    strokeLineWidth = 0.210777f, strokeLineCap = Butt, strokeLineJoin = Miter,
                    strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveToRelative(13.3271f, 6.3894f)
                curveToRelative(-0.0632f, 0.1897f, -0.1265f, 2.8244f, -0.1265f, 5.8596f)
                curveToRelative(0.0f, 4.0048f, 0.0632f, 5.5856f, 0.2529f, 5.7753f)
                curveToRelative(0.3794f, 0.3794f, 0.9696f, 0.2951f, 1.2225f, -0.1476f)
                curveToRelative(0.3162f, -0.6113f, 0.2951f, -11.2344f, -0.0422f, -11.5717f)
                curveToRelative(-0.3583f, -0.3583f, -1.1593f, -0.3162f, -1.3068f, 0.0843f)
                close()
            }
        }
        .build()
        return _detergent!!
    }

private var _detergent: ImageVector? = null

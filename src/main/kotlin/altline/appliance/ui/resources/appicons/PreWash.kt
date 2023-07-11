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

public val AppIcons.PreWash: ImageVector
    get() {
        if (_prewash != null) {
            return _prewash!!
        }
        _prewash = Builder(name = "PreWash", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveTo(0.0762f, 4.5911f)
                curveTo(-0.1768f, 4.7387f, 0.1394f, 6.0035f, 1.9524f, 11.8008f)
                curveTo(3.1329f, 15.6586f, 4.208f, 19.1369f, 4.3134f, 19.4953f)
                lineToRelative(0.1897f, 0.6957f)
                lineToRelative(7.3572f, -0.0f)
                lineToRelative(7.3572f, -0.0f)
                lineToRelative(0.8432f, -2.6983f)
                curveToRelative(2.4875f, -7.9896f, 3.7946f, -12.2058f, 3.921f, -12.5009f)
                curveToRelative(0.1265f, -0.3162f, -0.4427f, -0.7589f, -0.6746f, -0.527f)
                curveToRelative(-0.0632f, 0.0422f, -1.1173f, 3.394f, -2.3611f, 7.4415f)
                lineTo(18.6695f, 19.2423f)
                lineTo(11.9026f, 19.3056f)
                lineTo(5.1356f, 19.3477f)
                lineTo(4.2924f, 16.6705f)
                curveTo(3.8286f, 15.1737f, 2.8167f, 11.8851f, 2.0367f, 9.3343f)
                curveTo(1.2567f, 6.7835f, 0.5611f, 4.6333f, 0.4978f, 4.549f)
                curveToRelative(-0.0632f, -0.0843f, -0.2741f, -0.0632f, -0.4216f, 0.0422f)
                close()
            }
            path(fill = SolidColor(Color(0xFF000000)), stroke = SolidColor(Color(0x00000000)),
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero) {
                moveToRelative(11.3545f, 5.7717f)
                curveToRelative(-0.5059f, 0.4849f, -0.5059f, 11.2783f, 0.0f, 11.7631f)
                curveToRelative(0.4005f, 0.4005f, 0.7589f, 0.4216f, 1.0962f, 0.0843f)
                curveToRelative(0.3584f, -0.3584f, 0.3584f, -11.5734f, 0.0f, -11.9318f)
                curveToRelative(-0.3373f, -0.3373f, -0.6957f, -0.3162f, -1.0962f, 0.0843f)
                close()
            }
        }
            .build()
        return _prewash!!
    }

private var _prewash: ImageVector? = null

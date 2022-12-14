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

val AppIcons.PlayPause: ImageVector
    get() {
        if (_playpause != null) {
            return _playpause!!
        }
        _playpause = Builder(
            name = "PlayPause", defaultWidth = 12.0.dp, defaultHeight = 24.0.dp,
            viewportWidth = 12.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(3.0174f, 10.035f)
                lineTo(3.0174f, 2.0f)
                lineToRelative(5.9719f, 4.0175f)
                close()
                moveTo(3.0107f, 22.0f)
                lineToRelative(0.0f, -8.6864f)
                lineToRelative(1.6287f, 0.0f)
                lineToRelative(0.0f, 8.6864f)
                close()
                moveTo(7.3539f, 22.0f)
                lineToRelative(0.0f, -8.6864f)
                lineToRelative(1.6287f, 0.0f)
                lineToRelative(0.0f, 8.6864f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveToRelative(1.6568f, 11.0896f)
                lineToRelative(8.6864f, 0.0f)
                lineToRelative(0.0f, 0.9244f)
                lineTo(1.6568f, 12.014f)
                close()
            }
        }
            .build()
        return _playpause!!
    }

private var _playpause: ImageVector? = null

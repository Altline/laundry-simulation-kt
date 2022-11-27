package altline.appliance.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material.lightColors
import androidx.compose.ui.graphics.Color

val Teal200 = Color(0xff80cbc4)
val Teal200_Dark = Color(0xff4f9a94)
val Teal900 = Color(0xff004d40)
val DeepPurple200 = Color(0xffb39ddb)
val DeepPurple200_Dark = Color(0xff836fa9)

val Colors = lightColors(
    primary = Teal200,
    primaryVariant = Teal200_Dark,
    secondary = DeepPurple200,
    secondaryVariant = DeepPurple200_Dark,
    onPrimary = Color(0xFF111111),
    onSecondary = Color.Black,
    onSurface = Color.Black
)

val Colors.surfaceTinted: Color
    get() = Color.LightGray

val Colors.onSurfaceTinted: Color
    get() = Teal900
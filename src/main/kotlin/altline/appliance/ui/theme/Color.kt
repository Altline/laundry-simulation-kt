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
    get() = Color(0xffe0e0e0)

val Colors.onSurfaceTinted: Color
    get() = Teal900

object SubstanceColors {
    val Water = Color(0xffdffbff)
    val Mud = Color(0xff532f00)
    val Coffee = Color(0xff513c21)
    val Ketchup = Color(0xffa32a11)
    val CrudeOil = Color(0xff060400)

    val UltimateDetergent = Color(0xff00ffc7)
    val StrongDetergent = Color(0xff60ffdc)
    val BasicDetergent = Color(0xff96ffe8)
    val MildDetergent = Color(0xffbcfff0)
    val WeakDetergent = Color(0xffd9fff7)
    val BarelyDetergent = Color(0xffebfffb)
    val UselessDetergent = Color(0xffffffff)

    val UltimateSoftener = Color(0xff007fff)
    val StrongSoftener = Color(0xff44a1ff)
    val BasicSoftener = Color(0xff7fbeff)
    val MildSoftener = Color(0xffbbddff)
    val WeakSoftener = Color(0xffd8ebff)
    val BarelySoftener = Color(0xffe9f4ff)
    val UselessSoftener = Color(0xffffffff)
}
package altline.appliance.ui.resources

import altline.appliance.ui.resources.appicons.Stain
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.collections.List as ____KtList

public object AppIcons

private var __AllAssets: ____KtList<ImageVector>? = null

public val AppIcons.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets= listOf(Stain)
    return __AllAssets!!
  }

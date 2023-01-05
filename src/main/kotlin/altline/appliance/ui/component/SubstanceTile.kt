package altline.appliance.ui.component

import altline.appliance.substance.SubstanceType
import altline.appliance.ui.mapper.ColorMapper
import altline.appliance.ui.mapper.StringMapper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.koin.core.context.GlobalContext

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubstanceTile(
    substanceType: SubstanceType,
    size: Dp,
    selected: Boolean,
    showLabel: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorMapper = remember { GlobalContext.get().get<ColorMapper>() }
    val stringMapper = remember { GlobalContext.get().get<StringMapper>() }

    val border =
        if (selected) BorderStroke(3.dp, Color.LightGray)
        else BorderStroke(1.dp, Color.LightGray)

    val elevation = if (selected) 8.dp else 0.dp

    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            selected = selected,
            onClick = onClick,
            Modifier.size(size),
            shape = RoundedCornerShape(8.dp),
            color = colorMapper.mapSubstanceTypeToColor(substanceType),
            border = border,
            elevation = elevation
        ) {}

        if (showLabel) {
            Text(
                text = stringMapper.mapSubstanceTypeName(substanceType),
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center
            )
        }
    }
}
package altline.appliance.ui.component

import altline.appliance.substance.SubstanceType
import altline.appliance.ui.resources.get
import altline.appliance.ui.resources.strings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SubstancePicker(
    substanceTypes: List<SubstanceType>,
    selectedType: SubstanceType,
    onSubstancePick: (SubstanceType) -> Unit,
    onClose: () -> Unit
) {
    val tileSize = 60.dp

    Column {
        Button(
            onClick = onClose,
            Modifier.padding(start = 16.dp, top = 8.dp)
        ) {
            Text(strings["back_button"])
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(tileSize),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(substanceTypes) { type ->
                SubstanceTile(
                    substanceType = type,
                    size = tileSize,
                    selected = type == selectedType,
                    showLabel = true,
                    onClick = { onSubstancePick(type) }
                )
            }
        }
    }
}
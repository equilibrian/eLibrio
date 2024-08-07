package su.elibrio.mobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import su.elibrio.mobile.utils.LIBRARY_FILTERS

@Composable
fun LibraryFilters() {
    var selectedFilter by remember { mutableIntStateOf(0) }
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        LIBRARY_FILTERS.forEachIndexed { idx, filter ->
            FilterChip(
                selected = selectedFilter == idx,
                onClick = { selectedFilter = idx },
                label = { Text(text = stringResource(id = filter.title)) },
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedFilter == idx,
                    borderColor = Color.Transparent
                )
            )
        }
    }
}
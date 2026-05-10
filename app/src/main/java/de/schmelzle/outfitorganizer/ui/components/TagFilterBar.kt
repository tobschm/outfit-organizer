package de.schmelzle.outfitorganizer.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.schmelzle.outfitorganizer.model.Tag

@Composable
fun TagFilterBar(
    activeFilters: Set<Tag>,
    onToggle: (Tag) -> Unit
) {
    Surface(shadowElevation = 8.dp, color = Color.White) {
        LazyRow(contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) {
            items(Tag.entries) { tag ->
                val selected = tag in activeFilters
                FilterChip(
                    selected = selected,
                    onClick = { onToggle(tag) },
                    label = { Text(tag.label) },
                    modifier = Modifier.padding(end = 8.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
    }
}

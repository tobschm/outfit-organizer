package de.schmelzle.outfitorganizer.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import de.schmelzle.outfitorganizer.model.Outfit
import java.io.File

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OutfitCard(outfit: Outfit, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            AsyncImage(
                model = File(outfit.imagePath),
                contentDescription = "Outfit Bild",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
            )
            if (outfit.tags.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                FlowRow(modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)) {
                    outfit.tags.forEach { tag ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(tag.label, fontSize = 10.sp) },
                            modifier = Modifier.padding(end = 4.dp, bottom = 2.dp),
                            colors = SuggestionChipDefaults.suggestionChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    }
                }
            } else {
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

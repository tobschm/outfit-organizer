package de.schmelzle.outfitorganizer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.schmelzle.outfitorganizer.model.Outfit
import de.schmelzle.outfitorganizer.model.Tag
import java.io.File

private const val SWIPE_THRESHOLD = 80f

@Composable
fun ImageDetailOverlay(
    outfit: Outfit?,
    onDismiss: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onDelete: () -> Unit,
    onTagToggle: (Tag) -> Unit
) {
    AnimatedVisibility(
        visible = outfit != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        if (outfit == null) return@AnimatedVisibility

        var dragAccum by remember(outfit.id) { mutableFloatStateOf(0f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.82f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {}
                ) {
                    items(Tag.entries) { tag ->
                        val selected = tag in outfit.tags
                        FilterChip(
                            selected = selected,
                            onClick = { onTagToggle(tag) },
                            label = { Text(tag.label) },
                            modifier = Modifier.padding(end = 8.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color.White.copy(alpha = 0.15f),
                                labelColor = Color.White,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selected,
                                borderColor = Color.White.copy(alpha = 0.5f),
                                selectedBorderColor = Color.Transparent
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = File(outfit.imagePath),
                        contentDescription = "Outfit Bild groß",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .clickable(onClick = onDismiss)
                            .pointerInput(outfit.id) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        when {
                                            dragAccum < -SWIPE_THRESHOLD -> onNext()
                                            dragAccum > SWIPE_THRESHOLD -> onPrevious()
                                        }
                                        dragAccum = 0f
                                    },
                                    onDragCancel = { dragAccum = 0f },
                                    onHorizontalDrag = { _, delta -> dragAccum += delta }
                                )
                            }
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White.copy(alpha = 0.15f),
                    contentColor = Color.White
                )
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Outfit löschen")
            }
        }
    }
}

package de.schmelzle.outfitorganizer.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import de.schmelzle.outfitorganizer.model.Tag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ImportDialog(
    onDismiss: () -> Unit,
    onConfirm: (uri: Uri, tags: Set<Tag>) -> Unit
) {
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedTags by remember { mutableStateOf<Set<Tag>>(emptySet()) }
    var pickTriggered by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            selectedUri = uri
        } else {
            onDismiss()
        }
    }

    // Launch the picker once when the dialog first appears
    LaunchedEffect(Unit) {
        if (!pickTriggered) {
            pickTriggered = true
            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    // Only show the dialog UI after an image has been picked
    if (selectedUri != null) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(
                shape = MaterialTheme.shapes.large,
                tonalElevation = 6.dp,
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text("Tags auswählen", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(16.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Tag.entries.forEach { tag ->
                            val active = tag in selectedTags
                            FilterChip(
                                selected = active,
                                onClick = {
                                    selectedTags = selectedTags.toMutableSet().apply {
                                        if (active) remove(tag) else add(tag)
                                    }
                                },
                                label = { Text(tag.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                    Spacer(Modifier.height(24.dp))
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = onDismiss) { Text("Abbrechen") }
                        Spacer(Modifier.width(8.dp))
                        TextButton(onClick = {
                            onConfirm(selectedUri!!, selectedTags)
                            onDismiss()
                        }) { Text("Hinzufügen") }
                    }
                }
            }
        }
    }
}

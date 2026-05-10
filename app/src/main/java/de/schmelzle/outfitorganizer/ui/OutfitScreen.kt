package de.schmelzle.outfitorganizer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import de.schmelzle.outfitorganizer.ui.components.ImageDetailOverlay
import de.schmelzle.outfitorganizer.ui.components.ImportDialog
import de.schmelzle.outfitorganizer.ui.components.OutfitGallery
import de.schmelzle.outfitorganizer.ui.components.TagFilterBar
import de.schmelzle.outfitorganizer.ui.components.TopBar

@Composable
fun OutfitScreen(
    modifier: Modifier = Modifier,
    viewModel: OutfitViewModel = viewModel()
) {
    val outfits by viewModel.filteredOutfits.collectAsState()
    val activeFilters by viewModel.activeFilters.collectAsState()
    val selectedIndex by viewModel.selectedIndex.collectAsState()

    var showImportDialog by remember { mutableStateOf(false) }

    val selectedOutfit = selectedIndex?.let { outfits.getOrNull(it) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(count = outfits.size, onAddClick = { showImportDialog = true })
            OutfitGallery(
                outfits = outfits,
                onOutfitClick = { index -> viewModel.selectOutfit(index) },
                modifier = Modifier.weight(1f)
            )
            TagFilterBar(
                activeFilters = activeFilters,
                onToggle = { viewModel.toggleFilter(it) }
            )
        }

        ImageDetailOverlay(
            outfit = selectedOutfit,
            onDismiss = { viewModel.clearSelection() },
            onNext = { viewModel.showNext() },
            onPrevious = { viewModel.showPrevious() },
            onDelete = { viewModel.deleteSelectedOutfit() },
            onTagToggle = { viewModel.toggleOutfitTag(it) }
        )
    }

    if (showImportDialog) {
        ImportDialog(
            onDismiss = { showImportDialog = false },
            onConfirm = { uri, tags -> viewModel.importOutfit(uri, tags) }
        )
    }
}

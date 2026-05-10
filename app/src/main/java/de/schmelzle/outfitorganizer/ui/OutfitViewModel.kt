package de.schmelzle.outfitorganizer.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import de.schmelzle.outfitorganizer.data.OutfitDatabase
import de.schmelzle.outfitorganizer.data.OutfitRepository
import de.schmelzle.outfitorganizer.model.Outfit
import de.schmelzle.outfitorganizer.model.Tag
import de.schmelzle.outfitorganizer.util.ImageStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class OutfitViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = OutfitRepository(
        OutfitDatabase.getInstance(application).outfitDao()
    )

    val activeFilters = MutableStateFlow<Set<Tag>>(emptySet())

    val filteredOutfits: StateFlow<List<Outfit>> = combine(
        repository.allOutfits,
        activeFilters
    ) { outfits, filters ->
        if (filters.isEmpty()) outfits
        else outfits.filter { outfit -> filters.all { it in outfit.tags } }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // Index into filteredOutfits; null = overlay not visible
    private val _selectedIndex = MutableStateFlow<Int?>(null)
    val selectedIndex: StateFlow<Int?> = _selectedIndex

    fun toggleFilter(tag: Tag) {
        activeFilters.value = activeFilters.value.toMutableSet().apply {
            if (contains(tag)) remove(tag) else add(tag)
        }
        // If the selected outfit is no longer in the filtered list, close the overlay
        _selectedIndex.value?.let { idx ->
            if (idx >= filteredOutfits.value.size) _selectedIndex.value = null
        }
    }

    fun selectOutfit(index: Int) {
        _selectedIndex.value = index
    }

    fun clearSelection() {
        _selectedIndex.value = null
    }

    fun showNext() {
        val current = _selectedIndex.value ?: return
        val max = filteredOutfits.value.lastIndex
        _selectedIndex.value = if (current < max) current + 1 else null
    }

    fun showPrevious() {
        val current = _selectedIndex.value ?: return
        _selectedIndex.value = if (current > 0) current - 1 else null
    }

    fun deleteSelectedOutfit() {
        val index = _selectedIndex.value ?: return
        val outfit = filteredOutfits.value.getOrNull(index) ?: return
        viewModelScope.launch {
            repository.deleteOutfit(outfit.id)
            ImageStorage.delete(outfit.imagePath)
            // After deletion, the list will update via Flow.
            // Adjust the selected index: stay at same index (now points to next item),
            // or close if the list becomes empty or we were at the last item.
            val newSize = filteredOutfits.value.size - 1
            _selectedIndex.value = when {
                newSize == 0 -> null
                index >= newSize -> newSize - 1
                else -> index
            }
        }
    }

    fun toggleOutfitTag(tag: Tag) {
        val index = _selectedIndex.value ?: return
        val outfit = filteredOutfits.value.getOrNull(index) ?: return
        val newTags = outfit.tags.toMutableSet().apply {
            if (contains(tag)) remove(tag) else add(tag)
        }
        viewModelScope.launch {
            repository.updateOutfitTags(outfit.id, newTags)
        }
    }

    fun importOutfit(uri: Uri, tags: Set<Tag>) {
        viewModelScope.launch {
            val path = ImageStorage.copyToAppStorage(getApplication(), uri)
            repository.insertOutfit(path, tags)
        }
    }
}

package de.schmelzle.outfitorganizer.data

import de.schmelzle.outfitorganizer.model.Outfit
import de.schmelzle.outfitorganizer.model.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OutfitRepository(private val dao: OutfitDao) {

    val allOutfits: Flow<List<Outfit>> = dao.getAllOutfits().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun insertOutfit(imagePath: String, tags: Set<Tag>): Long {
        val entity = OutfitEntity(
            imagePath = imagePath,
            tags = tags.joinToString(",") { it.name }
        )
        return dao.insertOutfit(entity)
    }

    suspend fun deleteOutfit(id: Long) {
        dao.deleteOutfit(id)
    }

    suspend fun updateOutfitTags(id: Long, tags: Set<Tag>) {
        dao.updateOutfitTags(id, tags.joinToString(",") { it.name })
    }

    private fun OutfitEntity.toDomain(): Outfit {
        val tagSet = if (tags.isBlank()) emptySet()
        else tags.split(",").mapNotNullTo(mutableSetOf()) {
            runCatching { Tag.valueOf(it) }.getOrNull()
        }
        return Outfit(id = id, imagePath = imagePath, tags = tagSet)
    }
}

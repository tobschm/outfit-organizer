package de.schmelzle.outfitorganizer.model

data class Outfit(
    val id: Long,
    val imagePath: String,
    val tags: Set<Tag>
)

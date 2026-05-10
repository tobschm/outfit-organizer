package de.schmelzle.outfitorganizer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface OutfitDao {
    @Query("SELECT * FROM outfits ORDER BY id DESC")
    fun getAllOutfits(): Flow<List<OutfitEntity>>

    @Insert
    suspend fun insertOutfit(outfit: OutfitEntity): Long

    @Query("DELETE FROM outfits WHERE id = :id")
    suspend fun deleteOutfit(id: Long)

    @Query("UPDATE outfits SET tags = :tags WHERE id = :id")
    suspend fun updateOutfitTags(id: Long, tags: String)
}

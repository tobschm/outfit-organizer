package de.schmelzle.outfitorganizer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OutfitEntity::class], version = 1, exportSchema = false)
abstract class OutfitDatabase : RoomDatabase() {
    abstract fun outfitDao(): OutfitDao

    companion object {
        @Volatile private var INSTANCE: OutfitDatabase? = null

        fun getInstance(context: Context): OutfitDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    OutfitDatabase::class.java,
                    "outfits.db"
                ).build().also { INSTANCE = it }
            }
    }
}

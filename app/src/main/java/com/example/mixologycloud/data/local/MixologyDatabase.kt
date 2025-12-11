package com.example.mixologycloud.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CocktailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MixologyDatabase : RoomDatabase() {
    abstract fun cocktailDao(): CocktailDao
}

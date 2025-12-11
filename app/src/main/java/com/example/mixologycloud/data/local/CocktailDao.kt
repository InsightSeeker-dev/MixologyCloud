package com.example.mixologycloud.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {
    
    @Query("SELECT * FROM cocktails ORDER BY timestamp DESC")
    fun getAllCocktails(): Flow<List<CocktailEntity>>
    
    @Query("SELECT * FROM cocktails WHERE id = :cocktailId")
    suspend fun getCocktailById(cocktailId: String): CocktailEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktail: CocktailEntity)
    
    @Query("DELETE FROM cocktails")
    suspend fun deleteAllCocktails()
    
    @Query("DELETE FROM cocktails WHERE id = :cocktailId")
    suspend fun deleteCocktailById(cocktailId: String)
    
    @Query("SELECT COUNT(*) FROM cocktails")
    fun getCocktailCount(): Flow<Int>
}

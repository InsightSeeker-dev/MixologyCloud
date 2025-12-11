package com.example.mixologycloud.data.repository

import com.example.mixologycloud.data.local.CocktailDao
import com.example.mixologycloud.data.mapper.toData
import com.example.mixologycloud.data.mapper.toEntity
import com.example.mixologycloud.data.model.CocktailData
import com.example.mixologycloud.data.remote.CocktailApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CocktailRepository @Inject constructor(
    private val apiService: CocktailApiService,
    private val cocktailDao: CocktailDao
) {
    
    fun getAllCocktails(): Flow<List<CocktailData>> {
        return cocktailDao.getAllCocktails()
            .map { entities -> entities.map { it.toData() } }
    }
    
    suspend fun getCocktailById(cocktailId: String): CocktailData? {
        return cocktailDao.getCocktailById(cocktailId)?.toData()
    }
    
    suspend fun addRandomCocktail(): Result<CocktailData> {
        return try {
            val response = apiService.getRandomCocktail()
            val cocktailDto = response.drinks?.firstOrNull()
                ?: return Result.failure(Exception("No cocktail found"))
            
            val timestamp = System.currentTimeMillis()
            val cocktailData = cocktailDto.toData(timestamp)
            
            cocktailDao.insertCocktail(cocktailData.toEntity())
            
            Result.success(cocktailData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun deleteAllCocktails() {
        cocktailDao.deleteAllCocktails()
    }
    
    fun getCocktailCount(): Flow<Int> {
        return cocktailDao.getCocktailCount()
    }
}

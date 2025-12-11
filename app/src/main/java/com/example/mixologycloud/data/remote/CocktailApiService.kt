package com.example.mixologycloud.data.remote

import retrofit2.http.GET

interface CocktailApiService {
    
    @GET("api/json/v1/1/random.php")
    suspend fun getRandomCocktail(): CocktailApiResponse
    
    companion object {
        const val BASE_URL = "https://www.thecocktaildb.com/"
    }
}

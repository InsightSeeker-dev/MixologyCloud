package com.example.mixologycloud.data.remote

import com.google.gson.annotations.SerializedName

data class CocktailApiResponse(
    @SerializedName("drinks")
    val drinks: List<CocktailDto>?
)

data class CocktailDto(
    @SerializedName("idDrink")
    val id: String,
    
    @SerializedName("strDrink")
    val name: String,
    
    @SerializedName("strCategory")
    val category: String?,
    
    @SerializedName("strAlcoholic")
    val alcoholic: String?,
    
    @SerializedName("strGlass")
    val glass: String?,
    
    @SerializedName("strInstructions")
    val instructions: String?,
    
    @SerializedName("strDrinkThumb")
    val thumbnail: String?
)

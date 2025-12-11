package com.example.mixologycloud.data.model

data class CocktailData(
    val id: String,
    val name: String,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val instructions: String,
    val thumbnail: String,
    val timestamp: Long
)

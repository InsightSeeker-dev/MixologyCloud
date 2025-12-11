package com.example.mixologycloud.ui.model

data class CocktailUi(
    val id: String,
    val name: String,
    val category: String,
    val alcoholic: String,
    val glass: String,
    val instructions: String,
    val thumbnail: String,
    val insertionDay: String
)

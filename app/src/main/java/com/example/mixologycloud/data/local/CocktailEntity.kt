package com.example.mixologycloud.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cocktails")
data class CocktailEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    
    @ColumnInfo(name = "name")
    val name: String,
    
    @ColumnInfo(name = "category")
    val category: String,
    
    @ColumnInfo(name = "alcoholic")
    val alcoholic: String,
    
    @ColumnInfo(name = "glass")
    val glass: String,
    
    @ColumnInfo(name = "instructions")
    val instructions: String,
    
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)

package com.example.mixologycloud.data.mapper

import com.example.mixologycloud.data.local.CocktailEntity
import com.example.mixologycloud.data.model.CocktailData
import com.example.mixologycloud.data.remote.CocktailDto
import com.example.mixologycloud.ui.model.CocktailUi
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun CocktailDto.toData(timestamp: Long = System.currentTimeMillis()): CocktailData {
    return CocktailData(
        id = id,
        name = name,
        category = category.orEmpty(),
        alcoholic = alcoholic.orEmpty(),
        glass = glass.orEmpty(),
        instructions = instructions.orEmpty(),
        thumbnail = thumbnail.orEmpty(),
        timestamp = timestamp
    )
}

fun CocktailEntity.toData(): CocktailData {
    return CocktailData(
        id = id,
        name = name,
        category = category,
        alcoholic = alcoholic,
        glass = glass,
        instructions = instructions,
        thumbnail = thumbnail,
        timestamp = timestamp
    )
}

fun CocktailData.toEntity(): CocktailEntity {
    return CocktailEntity(
        id = id,
        name = name,
        category = category,
        alcoholic = alcoholic,
        glass = glass,
        instructions = instructions,
        thumbnail = thumbnail,
        timestamp = timestamp
    )
}

fun CocktailData.toUi(): CocktailUi {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val insertionDay = dateFormat.format(Date(timestamp))
    
    return CocktailUi(
        id = id,
        name = name,
        category = category,
        alcoholic = alcoholic,
        glass = glass,
        instructions = instructions,
        thumbnail = thumbnail,
        insertionDay = insertionDay
    )
}

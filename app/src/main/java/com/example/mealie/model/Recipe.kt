package com.example.mealie.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val imageUrl: String?,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepTimeMinutes: Int? = null,
    val cookTimeMinutes: Int? = null,
    val servings: Int? = null,
    val isFavorite: Boolean = false,
)



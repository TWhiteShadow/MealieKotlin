package com.example.mealie.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mealie.model.Recipe

@Database(entities = [Recipe::class], version = 1)
abstract class AppDb : RoomDatabase() {
    abstract fun dao(): RecipeDao
}
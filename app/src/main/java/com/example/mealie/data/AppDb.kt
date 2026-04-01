package com.example.mealie.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.mealie.model.Recipe

@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDb : RoomDatabase() {
    abstract fun dao(): RecipeDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getInstance(context: android.content.Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = androidx.room.Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
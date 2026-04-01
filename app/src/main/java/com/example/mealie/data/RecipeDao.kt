package com.example.mealie.data
import androidx.room.*
import com.example.mealie.model.Recipe


import kotlinx.coroutines.flow.Flow
@Dao
public interface RecipeDao {

    @Query("SELECT * FROM Recipe ORDER BY title COLLATE NOCASE ASC")
    fun getAll(): Flow<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE isFavorite = 1 ORDER BY title COLLATE NOCASE ASC")
    fun getFavorites(): Flow<List<Recipe>>

    @Query("SELECT * FROM Recipe WHERE id = :id")
    fun getById(id: Int): Flow<Recipe?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)
}

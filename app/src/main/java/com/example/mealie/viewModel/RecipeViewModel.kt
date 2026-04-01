package com.example.mealie.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mealie.data.RecipeDao
import com.example.mealie.model.Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val dao: RecipeDao,
) : ViewModel() {

    val recipes: StateFlow<List<Recipe>> = dao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val favoriteRecipes: StateFlow<List<Recipe>> = dao.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun recipeById(id: Int): StateFlow<Recipe?> = dao.getById(id)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            dao.insert(recipe)
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch {
            dao.update(recipe)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch {
            dao.delete(recipe)
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            dao.update(recipe.copy(isFavorite = !recipe.isFavorite))
        }
    }
}
package com.example.mealie.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mealie.data.RecipeDao

class RecipeViewModelFactory(
    private val dao: RecipeDao,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecipeViewModel(dao) as T
        }
        throw IllegalArgumentException("Classe ViewModel inconnue")
    }
}
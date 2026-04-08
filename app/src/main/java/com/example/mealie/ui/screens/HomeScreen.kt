package com.example.mealie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealie.ui.components.RecipeCard
import com.example.mealie.viewModel.RecipeViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    recipeViewModel: RecipeViewModel
) {
    val recipes by recipeViewModel.recipes.collectAsState()

    if (recipes.isEmpty()) return

    val firstRecipe = recipes.first()
    val otherRecipes = recipes.drop(1)

    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(13.dp),
        verticalArrangement = Arrangement.spacedBy(13.dp),
    ) {

        item(span = { GridItemSpan(2) }) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Bonjour, Chef 👋",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Que cuisinons nous aujourd'hui ?",
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }

        item(span = { GridItemSpan(2) }) {
            RecipeCard(
                recipe = firstRecipe,
                big = true,
                onClick = {
                    navController.navigate("recipe_product/${firstRecipe.id}")
                }
            )
        }

        items(
            items = otherRecipes,
            key = { it.id }
        ) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = {
                    navController.navigate("recipe_product/${recipe.id}")
                }
            )
        }
    }
}

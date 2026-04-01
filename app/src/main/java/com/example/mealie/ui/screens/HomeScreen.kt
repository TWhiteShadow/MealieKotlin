package com.example.mealie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mealie.MainViewModel
import com.example.mealie.R
import com.example.mealie.viewModel.RecipeViewModel

@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel, recipeViewModel: RecipeViewModel) {
    val recipes by recipeViewModel.recipes.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(recipes) { recipe ->
            Card(
                onClick = {
                    navController.navigate("recipe_product/${recipe.id}")
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column {
                    if (recipe.imageUrl != null) {
                        AsyncImage(
                            model = recipe.imageUrl,
                            contentDescription = "Meal Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.pasta),
                            contentDescription = "Meal Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    }
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = recipe.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

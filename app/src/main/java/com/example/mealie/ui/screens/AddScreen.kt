package com.example.mealie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mealie.model.Recipe
import com.example.mealie.viewModel.RecipeViewModel

@Composable
fun AddScreen(navController: NavController, recipeViewModel: RecipeViewModel) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    
    var ingredientsList by remember { mutableStateOf(listOf<String>()) }
    var currentIngredient by remember { mutableStateOf("") }
    
    var stepsList by remember { mutableStateOf(listOf<String>()) }
    var currentStep by remember { mutableStateOf("") }
    
    var prepTime by remember { mutableStateOf("") }
    var cookTime by remember { mutableStateOf("") }
    var servings by remember { mutableStateOf("") }

    val isPrepTimeError = prepTime.isNotBlank() && prepTime.toIntOrNull() == null
    val isCookTimeError = cookTime.isNotBlank() && cookTime.toIntOrNull() == null
    val isServingsError = servings.isNotBlank() && servings.toIntOrNull() == null

    val isValid = title.isNotBlank() && description.isNotBlank() && !isPrepTimeError && !isCookTimeError && !isServingsError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") },
            modifier = Modifier.fillMaxWidth()
        )
        
        // Ingredients
        Text(
            text = "Ingredients",
            modifier = Modifier.align(Alignment.Start).padding(top = 8.dp),
            style = MaterialTheme.typography.titleSmall
        )
        ingredientsList.forEachIndexed { index, ingredient ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("- $ingredient", modifier = Modifier.weight(1f))
                IconButton(onClick = { 
                    ingredientsList = ingredientsList.toMutableList().apply { removeAt(index) } 
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Remove")
                }
            }
        }
        OutlinedTextField(
            value = currentIngredient,
            onValueChange = { currentIngredient = it },
            label = { Text("Add Ingredient") },
            placeholder = { Text("Type and press Enter") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (currentIngredient.isNotBlank()) {
                        ingredientsList = ingredientsList + currentIngredient.trim()
                        currentIngredient = ""
                    }
                }
            )
        )

        // Steps
        Text(
            text = "Steps",
            modifier = Modifier.align(Alignment.Start).padding(top = 8.dp),
            style = MaterialTheme.typography.titleSmall
        )
        stepsList.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("${index + 1}. $step", modifier = Modifier.weight(1f))
                IconButton(onClick = { 
                    stepsList = stepsList.toMutableList().apply { removeAt(index) } 
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Remove")
                }
            }
        }
        OutlinedTextField(
            value = currentStep,
            onValueChange = { currentStep = it },
            label = { Text("Add Step") },
            placeholder = { Text("Type and press Enter") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (currentStep.isNotBlank()) {
                        stepsList = stepsList + currentStep.trim()
                        currentStep = ""
                    }
                }
            )
        )

        // Numeric fields on one row
        Row (
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            OutlinedTextField(
                value = prepTime,
                onValueChange = { prepTime = it },
                label = { Text("Prep (min)") },
                modifier = Modifier.weight(1f),
                isError = isPrepTimeError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = { if (isPrepTimeError) Text("Invalid") }
            )
            OutlinedTextField(
                value = cookTime,
                onValueChange = { cookTime = it },
                label = { Text("Cook (min)") },
                modifier = Modifier.weight(1f),
                isError = isCookTimeError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = { if (isCookTimeError) Text("Invalid") }
            )
        }
            OutlinedTextField(
                value = servings,
                onValueChange = { servings = it },
                label = { Text("Servings") },
                modifier = Modifier.fillMaxWidth(),
                isError = isServingsError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = { if (isServingsError) Text("Invalid") }
            )

        Button(
            onClick = {
                val finalIngredients = ingredientsList + if (currentIngredient.isNotBlank()) listOf(currentIngredient.trim()) else emptyList()
                val finalSteps = stepsList + if (currentStep.isNotBlank()) listOf(currentStep.trim()) else emptyList()
                
                val recipe = Recipe(
                    title = title.trim(),
                    description = description.trim(),
                    imageUrl = imageUrl.ifBlank { null },
                    ingredients = finalIngredients,
                    steps = finalSteps,
                    prepTimeMinutes = prepTime.toIntOrNull(),
                    cookTimeMinutes = cookTime.toIntOrNull(),
                    servings = servings.toIntOrNull()
                )
                recipeViewModel.addRecipe(recipe)
                navController.popBackStack()
            },
            enabled = isValid,
            modifier = Modifier.padding(top = 16.dp, bottom = 32.dp)
        ) {
            Text("Save Recipe")
        }
    }
}

package com.example.mealie.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Whatshot
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mealie.R
import com.example.mealie.viewModel.RecipeViewModel

@Composable
fun RecipeProductScreen(
    navController: NavController,
    recipeId: Int,
    recipeViewModel: RecipeViewModel,
) {

    //on recupere le flux de donnee et on le transforme en state (compose)
    val recipeFlow = remember(recipeId) { recipeViewModel.recipeById(recipeId) }
    val recipe by recipeFlow.collectAsState()

    //petit affichage de chargement
    if (recipe == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    //pour recuperer recipe non null
    val recipenonull = recipe!!
    var isFavorite by remember(recipenonull.id) { mutableStateOf(recipenonull.isFavorite) }
    var showIngredients by remember(recipeId) { mutableStateOf(true) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        //photo + bouton de retour
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)),
            ) {
            //image et si c'est nul image pasta
            if (!recipenonull.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = recipenonull.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.pasta),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            //bouton de retour
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .padding(12.dp)
                    .align(Alignment.TopStart)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = CircleShape,
                    ),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        }


        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                //titre de la recette
                Text(
                    text = recipenonull.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )

                //ajout au favoris
                IconButton(onClick = {
                    isFavorite = !isFavorite
                    recipeViewModel.toggleFavorite(recipenonull)
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                        tint = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //description
            Text(
                text = recipenonull.description,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // les 3 infobadge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InfoBadge(
                    icon = Icons.Outlined.Timer,
                    value = recipenonull.prepTimeMinutes.toString() + " min",
                    text = "Préparation",
                    modifier = Modifier.weight(1f),
                )
                InfoBadge(
                    icon = Icons.Outlined.Whatshot,
                    value = recipenonull.cookTimeMinutes.toString() + " min",
                    text = "Cuisson",
                    modifier = Modifier.weight(1f),
                )
                InfoBadge(
                    icon = Icons.Outlined.Group,
                    value = recipenonull.servings.toString(),
                    text = "Portions",
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            // ingredients et etapes et vue qui change
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SelectableSectionCard(
                    text = "Ingrédients",
                    selected = showIngredients,
                    onClick = { showIngredients = true },
                    modifier = Modifier.weight(1f),
                )
                SelectableSectionCard(
                    text = "Étapes",
                    selected = !showIngredients,
                    onClick = { showIngredients = false },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showIngredients) {
                Text(
                    text = "Ingrédients",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                recipenonull.ingredients.forEach { ingredient ->
                    Text(
                        text = " - $ingredient",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp),
                    )
                }
            }
            else {
                Text(
                    text = "Étapes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                recipenonull.steps.forEachIndexed { index, etape ->
                    Text(
                        text = "${index + 1}. $etape",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 6.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}



@Composable
private fun InfoBadge(
    icon: ImageVector,
    value: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun SelectableSectionCard(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline

    Surface(
        onClick = onClick,
        //pour l'arrondi et l'arrondi de la colueur
        modifier = modifier.border(1.5.dp, borderColor, RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),

        shadowElevation = if (selected) 4.dp else 2.dp,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            //couleur du texte selectionne
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,

            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 14.dp)
                .fillMaxWidth(),
        )
    }
}



@Preview(showBackground = true)
@Composable
private fun InfoBadgePreview() {
    InfoBadge(
        icon = Icons.Outlined.Timer,
        value = "15",
        text = "Préparation",
        modifier = Modifier,
    )
}
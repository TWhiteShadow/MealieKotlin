package com.example.mealie.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.mealie.R
import com.example.mealie.model.Recipe

@Composable
fun RecipeCard(
    recipe: Recipe,
    big : Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(32.dp),
    ) {
        Column {
            if (!recipe.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = "Meal Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height( if (big) 200.dp else 100.dp)
                        .clip(RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp))
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.pasta),
                    contentDescription = "Meal Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(bottomEnd = 32.dp, bottomStart = 32.dp))
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.title,
                    maxLines = 3,
                    style = if (big) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = recipe.description,
                    style = if (big) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}


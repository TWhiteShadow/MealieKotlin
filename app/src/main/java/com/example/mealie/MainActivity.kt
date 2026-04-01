package com.example.mealie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mealie.data.AppDb
import com.example.mealie.screens.AddScreen
import com.example.mealie.screens.FavoritesScreen
import com.example.mealie.screens.HomeScreen
import com.example.mealie.screens.RecipeProductScreen
import com.example.mealie.ui.theme.MealieTheme
import com.example.mealie.viewModel.RecipeViewModel
import com.example.mealie.viewModel.RecipeViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MealieTheme {
                MyApp()
            }
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Add : BottomNavItem("add", Icons.Default.Add, "Add")
    object Favorites : BottomNavItem("favorites", Icons.Default.Favorite, "Favorites")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyApp(mainViewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val dao = AppDb.getInstance(context).dao()
    val recipeViewModel: RecipeViewModel = viewModel(factory = RecipeViewModelFactory(dao))

    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Add,
        BottomNavItem.Favorites
    )
    val uiState by mainViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(uiState.title) }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Home.route
            ) {
                composable(BottomNavItem.Home.route) {
                    HomeScreen(navController, mainViewModel, recipeViewModel)
                }
                composable(BottomNavItem.Add.route) {
                    AddScreen(navController, mainViewModel, recipeViewModel)
                }
                composable(BottomNavItem.Favorites.route) {
                    FavoritesScreen(navController, mainViewModel)
                }
                composable("recipe_product/{recipeId}") { backStackEntry ->
                    val recipeId = backStackEntry.arguments?.getString("recipeId") ?: "1"
                    RecipeProductScreen(navController, mainViewModel, recipeId)
                }
            }
        }
    }
}


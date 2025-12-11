package com.example.mixologycloud.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mixologycloud.ui.screens.CocktailDetailScreen
import com.example.mixologycloud.ui.screens.CocktailListScreen
import com.example.mixologycloud.ui.screens.FirebaseScreen
import com.example.mixologycloud.ui.screens.HomeScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CocktailList : Screen("cocktail_list")
    object CocktailDetail : Screen("cocktail_detail/{cocktailId}") {
        fun createRoute(cocktailId: String) = "cocktail_detail/$cocktailId"
    }
    object Firebase : Screen("firebase")
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCocktailList = {
                    navController.navigate(Screen.CocktailList.route)
                },
                onNavigateToFirebase = {
                    navController.navigate(Screen.Firebase.route)
                }
            )
        }
        
        composable(Screen.CocktailList.route) {
            CocktailListScreen(
                onNavigateToCocktailDetail = { cocktailId ->
                    navController.navigate(Screen.CocktailDetail.createRoute(cocktailId))
                }
            )
        }
        
        composable(
            route = Screen.CocktailDetail.route,
            arguments = listOf(
                navArgument("cocktailId") { type = NavType.StringType }
            )
        ) {
            CocktailDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Firebase.route) {
            FirebaseScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

package com.example.shire.ui.screens

import HomeScreenRent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "hoteles") {
        // Pantalla de Hoteles
        composable("hoteles") {
            HomeScreen(
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        // Pantalla de Vuelos
        composable("vuelos") {
            HomeScreenFlights(
                onNavigate = { route -> navController.navigate(route) }
            )
        }
        // Pantalla de Alquiler
        composable("alquiler") {
            HomeScreenRent(
                onNavigate = { route -> navController.navigate(route) }
            )
        }

        composable("searchHotel") {
            HotelsSearch(
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}
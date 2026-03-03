package com.example.shire.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shire.ui.components.BottomNavBar

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigateAction: (String) -> Unit = { route ->
        if (route == "hotel_details") {
            // Standard navigation for child screens
            navController.navigate(route)
        } else {
            // Tab navigation setup to prevent duplicates
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = currentRoute,
                onNavigate = navigateAction
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, 
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            // Página Principal
            composable("home") {
                HomeScreen(onNavigate = navigateAction)
            }
            // Vuelos
            composable("vuelos") {
                HomeScreenFlights(onNavigate = navigateAction)
            }
            // Alquiler
            composable("alquiler") {
                HomeScreenRent(onNavigate = navigateAction)
            }
            // Hoteles Búsqueda (Actual)
            composable("searchHotel") {
                HotelsSearch(onNavigate = navigateAction)
            }
            // Detalles del Hotel
            composable("hotel_details") {
                HotelDetailsScreen(
                    onNavigateUp = { navController.popBackStack() }
                )
            }

            // --- New Placeholder Routes for Bottom Navigation ---
            composable("trips") {
                TripsScreen(onNavigate = navigateAction)
            }
            composable("trip_details/{tripId}") { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                TripDetailsScreen(tripId = tripId, onNavigateUp = { navController.popBackStack() }, onNavigate = navigateAction)
            }
            composable("trip_gallery/{tripId}") { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
                TripGalleryScreen(tripId = tripId, onNavigateUp = { navController.popBackStack() })
            }
            composable("profile") {
                ProfileScreen(onNavigate = navigateAction)
            }
            composable("about") {
                AboutScreen(onNavigateUp = { navController.popBackStack() })
            }
            composable("terms") {
                TermsScreen(onNavigateUp = { navController.popBackStack() })
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Pantalla: $title")
    }
}

package com.example.shire.ui.view

import android.net.Uri
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
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.stringResource
import com.example.shire.R
import com.example.shire.ui.components.BottomNavBar

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val navigateAction: (String) -> Unit = { route ->
        val isChildRoute =
            route == "hotel_details" ||
                route.startsWith("trip_details") ||
                route.startsWith("trip_gallery") ||
                route.startsWith("create_trip") ||
                route == "about" ||
                route == "terms"

        if (isChildRoute) {
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
            startDestination = "tripsScreen",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // Página Trips
            composable("tripsScreen") {
                TripsScreen(onNavigate = navigateAction)
            }
            // Página Principal
            composable("home") {
                HomeScreen(onNavigate = navigateAction)
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
            composable(
                route = "create_trip?destination={destination}&startDate={startDate}&endDate={endDate}&adults={adults}&children={children}",
                arguments = listOf(
                    navArgument("destination") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("startDate") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("endDate") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("adults") {
                        type = NavType.IntType
                        defaultValue = 2
                    },
                    navArgument("children") {
                        type = NavType.IntType
                        defaultValue = 0
                    }
                )
            ) { backStackEntry ->
                val destination = backStackEntry.arguments?.getString("destination")?.let(Uri::decode)
                val startDate = backStackEntry.arguments?.getString("startDate")?.let(Uri::decode)
                val endDate = backStackEntry.arguments?.getString("endDate")?.let(Uri::decode)
                val adults = backStackEntry.arguments?.getInt("adults") ?: 2
                val children = backStackEntry.arguments?.getInt("children") ?: 0

                CreateTripScreen(
                    onNavigateUp = { navController.popBackStack() },
                    onNavigate = navigateAction,
                    initialDestination = destination,
                    initialStartDate = startDate,
                    initialEndDate = endDate,
                    initialAdults = adults,
                    initialChildren = children
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = stringResource(id = R.string.screen_placeholder, title))
    }
}

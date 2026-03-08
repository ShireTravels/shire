package com.example.shire.ui.screens

import com.example.shire.ui.components.TripCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shire.ui.theme.ShireTheme

data class UpcomingTrip(
    val id: String,
    val title: String,
    val dates: String,
    val price: String,
    val progress: Float
)

@Composable
fun TripsScreen(onNavigate: (String) -> Unit) {
    val trips = listOf(
        UpcomingTrip("1", "Aventura en Tokio", "10 Mar - 18 Mar • 8 noches", "1.240€", 0.65f),
        UpcomingTrip("2", "Escapada a París", "5 Abr - 9 Abr • 4 noches", "990€", 0.30f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            // Header con Gradiente
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primaryContainer
                                )
                            )
                        )
                        .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp)
                ) {
                    Column {
                        Text(
                            text = "Buenos días, Victor \uD83D\uDC4B",
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "TUS VIAJES",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

            // Sección de Título de Lista
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Itinerarios activos",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { /* TODO */ }) {
                        Text(
                            text = "Ver historial",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }

            items(trips) { trip ->
                TripCard(trip = trip, onClick = { onNavigate("trip_details/" + trip.id) })
            }

            item {
                Text(
                    text = "Próximos Itinerarios",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
                )
            }

            items(trips) { trip ->
                TripCard(trip = trip, onClick = { onNavigate("trip_details/" + trip.id) })
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }

        // FAB
        FloatingActionButton(
            onClick = { onNavigate("create_trip") },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Añadir Viaje")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripsScreenPreview() {
    ShireTheme {
        TripsScreen(onNavigate = {})
    }
}

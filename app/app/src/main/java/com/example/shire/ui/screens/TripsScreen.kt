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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.theme.ShireTheme

data class UpcomingTrip(
    val id: String,
    val title: String,
    val dates: String,
    val price: String,
    val progress: Float
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripsScreen(onNavigate: (String) -> Unit) {
    val trips = listOf(
        UpcomingTrip("1", "Aventura en Tokio", "10 Mar - 18 Mar • 8 noches", "1.240€", 0.65f),
        UpcomingTrip("2", "Escapada a París", "5 Abr - 9 Abr • 4 noches", "990€", 0.30f)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF8F9FA), // Fondo ligeramente gris para que resalten las cards blancas
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigate("home") },
                containerColor = Color(0xFF006CE4),
                shape = CircleShape,
                modifier = Modifier.offset(y = 10.dp) // Ajuste para que no choque con la BottomNav
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir Viaje", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Start
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header con Gradiente y Estilo Moderno
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color(0xFF006CE4), Color(0xFF004BA0))
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Column {
                        Text(
                            text = "Buenos días, Victor \uD83D\uDC4B",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "SHIRE",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            lineHeight = 34.sp
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
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    TextButton(onClick = { /* TODO */ }) {
                        Text("Ver historial", color = Color(0xFF006CE4), fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Implementación usando tu ShireCard personalizada
            items(trips) { trip ->
                TripCard(trip = trip, onClick = { onNavigate("trip_details/" + trip.id) })
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
                        text = "Proximos Itinerarios",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1C1E)
                    )
                    TextButton(onClick = { /* TODO */ }) {
                        Text("Ver historial", color = Color(0xFF006CE4), fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Implementación usando tu ShireCard personalizada
            items(trips) { trip ->
                TripCard(trip = trip, onClick = { onNavigate("trip_details/" + trip.id) })
            }


            // Espaciador final para no tapar contenido
            item { Spacer(modifier = Modifier.height(100.dp)) }
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


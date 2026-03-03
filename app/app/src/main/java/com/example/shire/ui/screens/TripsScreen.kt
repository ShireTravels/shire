package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.components.SectionTitle
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
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO Add trip */ },
                containerColor = Color(0xFF006CE4),
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 80.dp) // Avoid bottom nav bar overlap
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir Viaje", tint = Color.White)
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Content
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF006CE4))
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Buenos días, Alex \uD83D\uDC4B",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color(0xFFFF9800), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("AJ", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mis Viajes",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Body
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Próximos Viajes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { /* TODO */ }) {
                        Text("Ver todos →", color = Color(0xFF006CE4))
                    }
                }
            }

            items(trips) { trip ->
                TripCard(trip = trip, onClick = { onNavigate("trip_details/${trip.id}") })
            }

            item {
                Spacer(modifier = Modifier.height(100.dp)) // Padding for BottomNav & FAB
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripCard(trip: UpcomingTrip, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            // Placeholder Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Imagen del destino", color = Color.Gray)
            }

            // Info section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = trip.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = trip.dates,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = trip.price,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF008009)
                    )
                    
                    // Simple Progress Bar placeholder
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LinearProgressIndicator(
                            progress = trip.progress,
                            modifier = Modifier
                                .width(100.dp)
                                .height(8.dp)
                                .padding(end = 8.dp),
                            color = Color(0xFF006CE4),
                            trackColor = Color(0xFFE0E0E0),
                        )
                        Text(
                            text = "${(trip.progress * 100).toInt()}%",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
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

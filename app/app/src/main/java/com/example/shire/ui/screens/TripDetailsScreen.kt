package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shire.ui.theme.ShireTheme

data class ItineraryItem(
    val time: String,
    val title: String,
    val subtitle: String,
    val cost: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color
)

data class ItineraryDay(
    val dayTitle: String,
    val items: List<ItineraryItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(tripId: String, onNavigateUp: () -> Unit, onNavigate: (String) -> Unit) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Itinerario", "Galería", "Presupuesto", "Notas")

    val itinerary = listOf(
        ItineraryDay(
            "DÍA 1 - 10 MAR",
            listOf(
                ItineraryItem("08:00", "Vuelo BCN → NRT", "Vueling VY7182 · Terminal 1", "420€", Icons.Default.Flight, Color(0xFF1976D2), Color(0xFFE3F2FD)),
                ItineraryItem("22:30", "Check-in · Shinjuku Hotel", "Shinjuku, Tokyo · 8k", "95€/nit", Icons.Default.Hotel, Color(0xFFD84315), Color(0xFFFBE9E7))
            )
        ),
        ItineraryDay(
            "DÍA 2 - 11 MAR",
            listOf(
                ItineraryItem("09:00", "Templo Senso-ji", "Asakusa · 2h visita", "Gratis", Icons.Default.Place, Color(0xFFC2185B), Color(0xFFFCE4EC)),
                ItineraryItem("13:00", "Ramen Ippudo", "Shibuya · Reserva hecha", "18€", Icons.Default.Restaurant, Color(0xFFFBC02D), Color(0xFFFFF9C4))
            )
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Viaje", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Image Box
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = "Aventura en Tokio",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "10 Mar - 18 Mar • 8 noches",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Stats Row
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem("8", "NOCHES")
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        StatItem("1.240€", "PRESUPUESTO")
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        StatItem("14", "ACTIVIDADES")
                    }
                }
            }

            // Custom Tab Row
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { 
                                selectedTabIndex = index 
                                if (title == "Galería") {
                                    onNavigate("trip_gallery/$tripId")
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    style = if (selectedTabIndex == index) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Itinerary Content
            if (selectedTabIndex == 0) {
                itinerary.forEach { day ->
                    item {
                        Text(
                            text = day.dayTitle,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }
                    items(day.items) { item ->
                        ItineraryItemRow(item)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value, 
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, 
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label, 
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, 
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ItineraryItemRow(item: ItineraryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = item.time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(48.dp)
        )
        
        Surface(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(40.dp),
            shape = CircleShape,
            color = item.iconBg
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = item.icon, 
                    contentDescription = null, 
                    tint = item.iconTint, 
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title, 
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold, 
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.subtitle, 
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.cost, 
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold, 
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripDetailsScreenPreview() {
    ShireTheme {
        TripDetailsScreen(tripId = "1", onNavigateUp = {}, onNavigate = {})
    }
}

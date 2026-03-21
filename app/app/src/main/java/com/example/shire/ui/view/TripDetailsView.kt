package com.example.shire.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.TripsDetailsViewModel

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
fun TripDetailsScreen(
    tripId: String, 
    onNavigateUp: () -> Unit, 
    onNavigate: (String) -> Unit,
    viewModel: TripsDetailsViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Itinerario", "Galería", "Presupuesto", "Notas")

    val trip = viewModel.getTrip(tripId.toIntOrNull() ?: 0)

    val itinerary = remember(trip) {
        if (trip == null) return@remember emptyList<ItineraryDay>()

        // Pre-compute first and last day per hotel to show Check-in / Check-out only once
        val hotelFirstDay = mutableMapOf<Int, Int>()
        val hotelLastDay = mutableMapOf<Int, Int>()
        trip.hotel.forEach { (day, hotelId) ->
            hotelFirstDay[hotelId] = minOf(hotelFirstDay[hotelId] ?: day, day)
            hotelLastDay[hotelId] = maxOf(hotelLastDay[hotelId] ?: day, day)
        }

        // The checkout day is the day AFTER the last hotel night
        val checkoutDay = hotelLastDay.mapValues { (_, lastDay) -> lastDay + 1 }

        val maxDay = listOf(
            trip.hotel.keys.maxOrNull() ?: 0,
            trip.flight.keys.maxOrNull() ?: 0,
            trip.car.keys.maxOrNull() ?: 0,
            trip.places.keys.maxOrNull() ?: 0,
            checkoutDay.values.maxOrNull() ?: 0
        ).maxOrNull() ?: 0

        val days = mutableListOf<ItineraryDay>()

        for (day in 1..maxDay) {
            val items = mutableListOf<ItineraryItem>()

            trip.flight[day]?.let { id ->
                viewModel.getFlight(id)?.let { flight ->
                    items.add(ItineraryItem("08:00", "Vuelo ${flight.flightNumber}", "${flight.departureCity} → ${flight.arrivalCity} · Terminal ${flight.terminal}", "${flight.price}€", Icons.Default.Flight, Color(0xFF1976D2), Color(0xFFE3F2FD)))
                }
            }

            // Show Check-in only on the first day of the hotel stay
            trip.hotel[day]?.let { hotelId ->
                if (hotelFirstDay[hotelId] == day) {
                    viewModel.getHotel(hotelId)?.let { hotel ->
                        items.add(ItineraryItem("14:00", "Check-in · ${hotel.name}", "${hotel.location} · ${hotel.rating}⭐", "${hotel.price}€/nit", Icons.Default.Hotel, Color(0xFFD84315), Color(0xFFFBE9E7)))
                    }
                }
            }

            // Show Check-out on the day after the last hotel night
            checkoutDay.forEach { (hotelId, coDay) ->
                if (coDay == day) {
                    viewModel.getHotel(hotelId)?.let { hotel ->
                        items.add(ItineraryItem("12:00", "Check-out · ${hotel.name}", "${hotel.location} · ${hotel.rating}⭐", "", Icons.Default.Hotel, Color(0xFFD84315), Color(0xFFFBE9E7)))
                    }
                }
            }

            trip.places[day]?.let { id ->
                viewModel.getPlace(id)?.let { place ->
                    items.add(ItineraryItem("16:00", place.name, "${place.location} · ${place.type}", if (place.price == 0.0) "Gratis" else "${place.price}€", Icons.Default.Place, Color(0xFFC2185B), Color(0xFFFCE4EC)))
                }
            }

            if (items.isNotEmpty()) {
                days.add(ItineraryDay("DÍA $day", items))
            }
        }
        days
    }

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
                            text = trip?.title ?: "Viaje no encontrado",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = trip?.dates ?: "",
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
                        StatItem(trip?.hotel?.size?.toString() ?: "0", "NOCHES")
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        StatItem("${trip?.price ?: "0"}€", "PRESUPUESTO")
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        StatItem(trip?.places?.size?.toString() ?: "0", "ACTIVIDADES")
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

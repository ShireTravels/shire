package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val tabs = listOf("Itinerary", "Gallery", "Budget", "Notes")

    val itinerary = listOf(
        ItineraryDay(
            "DAY 1 - MAR 10",
            listOf(
                ItineraryItem("08:00", "Flight BCN → NRT", "Vueling VY7182 · Terminal 1", "€420", Icons.Default.Flight, Color(0xFF1976D2), Color(0xFFE3F2FD)),
                ItineraryItem("22:30", "Check-in · Shinjuku Hotel", "Shinjuku, Tokyo · 8k", "€95/nit", Icons.Default.Hotel, Color(0xFFD84315), Color(0xFFFBE9E7))
            )
        ),
        ItineraryDay(
            "DAY 2 - MAR 11",
            listOf(
                ItineraryItem("09:00", "Senso-ji Temple", "Asakusa · 2h visita", "Gratis", Icons.Default.Place, Color(0xFFC2185B), Color(0xFFFCE4EC)),
                ItineraryItem("13:00", "Ramen Ippudo", "Shibuya · Reserva hecha", "€18", Icons.Default.Restaurant, Color(0xFFFBC02D), Color(0xFFFFF9C4))
            )
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            // Keep empty so main AppNavigation bottom bar shows, 
            // but we add bottom padding in LazyColumn to avoid overlap
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
                        .height(220.dp)
                        .background(Color(0xFF3B2E6A)) // Purple placeholder background
                ) {
                    IconButton(
                        onClick = onNavigateUp,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                            .background(Color.White.copy(alpha = 0.3f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Tokyo Adventure",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Mar 10 - Mar 18 • 8 nights",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Stats Row
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatItem("8", "NIGHTS")
                    Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Color.LightGray)
                    StatItem("€1,240", "BUDGET")
                    Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Color.LightGray)
                    StatItem("14", "ACTIVITIES")
                }
                Divider(color = Color.LightGray, thickness = 1.dp)
            }

            // Custom Tab Row
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = Color(0xFF006CE4),
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = Color(0xFF006CE4),
                            height = 3.dp
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { 
                                selectedTabIndex = index 
                                if (title == "Gallery") {
                                    onNavigate("trip_gallery/$tripId")
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedTabIndex == index) Color(0xFF006CE4) else Color.Gray
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
                            color = Color(0xFF006CE4),
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }
                    items(day.items) { item ->
                        ItineraryItemRow(item)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                
                item {
                    Spacer(modifier = Modifier.height(80.dp)) // Extra padding for BottomNav
                }
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.onBackground)
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
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
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.width(48.dp)
        )
        
        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(40.dp)
                .background(item.iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = item.icon, contentDescription = null, tint = item.iconTint, modifier = Modifier.size(20.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
            Text(text = item.subtitle, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.cost, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFFFF9800))
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

package com.example.shire.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.BottomCenter
    ) {
        // The background navbar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 16.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Slimmer
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clickable { onNavigate("home") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = if (currentRoute == "home") Color(0xFF006CE4) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Hotels
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clickable { onNavigate("searchHotel") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Hotels",
                        tint = if (currentRoute == "searchHotel" || currentRoute == "hoteles") Color(0xFF006CE4) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Empty space for the floating center button
                Spacer(modifier = Modifier.weight(1f))

                // Planes
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clickable { onNavigate("vuelos") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Planes",
                        tint = if (currentRoute == "vuelos") Color(0xFF006CE4) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Profile
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight().clickable { onNavigate("profile") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = if (currentRoute == "profile") Color(0xFF006CE4) else Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        // The overlapping circle button
        Box(
            modifier = Modifier
                .offset(y = 4.dp) // Keeps its center vertically aligned with the 56dp navbar
                .size(64.dp) // Slightly bigger than the 56dp navbar to overlay
                .background(Color(0xFF006CE4), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize().clickable { onNavigate("trips") },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Trips",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

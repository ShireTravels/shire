package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.theme.ShireTheme

data class GalleryPhoto(val id: Int, val color: Color, val isTop: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripGalleryScreen(tripId: String, onNavigateUp: () -> Unit) {
    val photos = listOf(
        GalleryPhoto(1, Color(0xFFFF4081), isTop = true),
        GalleryPhoto(2, Color(0xFF4FC3F7), isTop = false),
        GalleryPhoto(3, Color(0xFFFFB74D), isTop = false),
        GalleryPhoto(4, Color(0xFFF06292), isTop = false),
        GalleryPhoto(5, Color(0xFFBA68C8), isTop = false),
        GalleryPhoto(6, Color(0xFF64B5F6), isTop = false),
        GalleryPhoto(7, Color(0xFFFF8A65), isTop = false),
        GalleryPhoto(8, Color(0xFF81C784), isTop = false),
        GalleryPhoto(9, Color(0xFFAED581), isTop = false)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text("Tokyo Adventure", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("9 fotos · 234 MB", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    Button(
                        onClick = { /* TODO Add photos */ },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29B6F6)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.padding(end = 8.dp).height(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Añadir", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Filters Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(
                    selected = true,
                    onClick = { },
                    label = { Text("Todas", fontWeight = FontWeight.Medium) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF006CE4), selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = { Text("★ Top", fontWeight = FontWeight.Medium) },
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = { Text("Videos", fontWeight = FontWeight.Medium) },
                    shape = RoundedCornerShape(16.dp)
                )
            }

            // Info and Sort Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${photos.size} elementos", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                    Text(text = "Ordenar: Fecha", fontSize = 14.sp, color = Color(0xFF006CE4), fontWeight = FontWeight.Medium)
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF006CE4)) // Placeholder for sort icon
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Photo Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(photos.size) { index ->
                    GalleryPhotoCard(photo = photos[index])
                }
                
                // Add new placeholder card at the end
                item {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { }
                    ) {
                        // Drawing dashed border manually
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Transparent)
                                .padding(2.dp) // Slight inset
                        ) {
                            var isDarkTheme = MaterialTheme.colorScheme.surface == Color.Black
                            val borderColor = if (isDarkTheme) Color.LightGray else Color.Gray
                            
                            androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                                drawRoundRect(
                                    color = borderColor,
                                    style = Stroke(
                                        width = 3.dp.toPx(),
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                    ),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx())
                                )
                            }
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Añadir", tint = borderColor)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("AÑADIR", color = borderColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                // Extra spacer for bottom nav if needed
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
fun GalleryPhotoCard(photo: GalleryPhoto) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(photo.color)
            .clickable { }
    ) {
        // Red Close Icon
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(20.dp)
                .background(Color.Red, CircleShape)
                .clickable { },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Delete", tint = Color.White, modifier = Modifier.size(12.dp))
        }

        // Top Badge
        if (photo.isTop) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(6.dp)
                    .background(Color(0xFFFF9800), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color.White, modifier = Modifier.size(8.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = "TOP", color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripGalleryScreenPreview() {
    ShireTheme {
        TripGalleryScreen(tripId = "1", onNavigateUp = {})
    }
}

package com.example.shire.ui.view

import androidx.compose.foundation.background
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shire.R
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.TripGalleryViewModel

data class GalleryPhoto(val url: String, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripGalleryScreen(
    tripId: String,
    onNavigateUp: () -> Unit,
    viewModel: TripGalleryViewModel = hiltViewModel()
) {
    val trip by viewModel.trip.collectAsState()
    
    val photos = trip?.gallery?.mapIndexed { index, url ->
        // Generate a stable color based on the URL hash
        val colorInt = android.graphics.Color.HSVToColor(floatArrayOf((url.hashCode() % 360).toFloat(), 0.5f, 0.9f))
        GalleryPhoto(url = url, color = Color(colorInt))
    } ?: emptyList()

    var showAddDialog by remember { mutableStateOf(false) }
    var photoUrlToAdd by remember { mutableStateOf("") }
    
    var showWatchDialog by remember { mutableStateOf<GalleryPhoto?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(trip?.title ?: stringResource(id = R.string.gallery_title), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text(stringResource(id = R.string.photos_count, photos.size), fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(id = R.string.back_desc))
                    }
                },
                actions = {
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF29B6F6)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.padding(end = 8.dp).height(36.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(id = R.string.add_btn), fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                    label = { Text(stringResource(id = R.string.all_filter), fontWeight = FontWeight.Medium) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF006CE4), selectedLabelColor = Color.White),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = { Text(stringResource(id = R.string.top_filter), fontWeight = FontWeight.Medium) },
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = false,
                    onClick = { },
                    label = { Text(stringResource(id = R.string.videos_filter), fontWeight = FontWeight.Medium) },
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
                Text(text = stringResource(id = R.string.items_count, photos.size), fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { }) {
                    Text(text = stringResource(id = R.string.sort_date), fontSize = 14.sp, color = Color(0xFF006CE4), fontWeight = FontWeight.Medium)
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
                    GalleryPhotoCard(
                        photo = photos[index],
                        onClick = { showWatchDialog = photos[index] },
                        onRemove = { viewModel.removePhoto(photos[index].url) }
                    )
                }
                
                // Add new placeholder card at the end
                item {
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { showAddDialog = true }
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
                                Text(stringResource(id = R.string.add_cap_btn), color = borderColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                
                // Extra spacer for bottom nav if needed
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text(stringResource(id = R.string.add_photo_title)) },
            text = {
                OutlinedTextField(
                    value = photoUrlToAdd,
                    onValueChange = { photoUrlToAdd = it },
                    label = { Text(stringResource(id = R.string.image_url_label)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (photoUrlToAdd.isNotBlank()) {
                        viewModel.addPhoto(photoUrlToAdd)
                        photoUrlToAdd = ""
                    }
                    showAddDialog = false
                }) {
                    Text(stringResource(id = R.string.add_btn))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text(stringResource(id = R.string.cancel_btn))
                }
            }
        )
    }

    if (showWatchDialog != null) {
        AlertDialog(
            onDismissRequest = { showWatchDialog = null },
            confirmButton = {
                TextButton(onClick = { showWatchDialog = null }) { Text(stringResource(id = R.string.close_btn)) }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(showWatchDialog!!.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "PREVIEW",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = showWatchDialog!!.url,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
    }
}

@Composable
fun GalleryPhotoCard(photo: GalleryPhoto, onClick: () -> Unit, onRemove: () -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(photo.color)
            .clickable { onClick() }
    ) {
        // Red Close Icon
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(4.dp)
                .size(24.dp)
                .background(Color.Red.copy(alpha = 0.8f), CircleShape)
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(id = R.string.delete_desc), tint = Color.White, modifier = Modifier.size(12.dp))
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

package com.example.shire.ui.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import com.example.shire.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shire.ui.components.DestinationCard
import com.example.shire.ui.components.DestinationStepContent
import com.example.shire.ui.components.SectionTitle
import com.example.shire.ui.components.ShireButton
import com.example.shire.ui.components.ShireTextField
import com.example.shire.ui.viewmodel.CreateTripViewModel

data class PopularDestination(val city: String, val country: String, val imageRes: Int)
data class HotelHighlight(val name: String, val city: String, val pricePerNight: String, val rating: String)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    val viewModel: CreateTripViewModel = hiltViewModel()

    val recommendedDestinations = listOf(
        PopularDestination("Barcelona", "España", 0),
        PopularDestination("Paris", "Francia", 0),
        PopularDestination("Roma", "Italia", 0),
        PopularDestination("Londres", "Reino Unido", 0)
    )

    val getawayDestinations = listOf(
        PopularDestination("Lisboa", "Portugal", 0),
        PopularDestination("Praga", "República Checa", 0),
        PopularDestination("Amsterdam", "Países Bajos", 0),
        PopularDestination("Viena", "Austria", 0)
    )

    val topHotels = listOf(
        HotelHighlight("Grand Mare Hotel", "Barcelona", "139€ / noche", "9.1"),
        HotelHighlight("Skyline Boutique", "Madrid", "115€ / noche", "8.9"),
        HotelHighlight("Riviera Rooms", "Valencia", "98€ / noche", "8.7")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
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
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.splash_logo),
                                contentDescription = "Shire Logo",
                                modifier = Modifier.size(34.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Shire",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Encuentra ofertas, compara hoteles y organiza tu próxima escapada.",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            item {
                DestinationStepContent(
                    destination = viewModel.tripDestination,
                    availableDestinations = viewModel.availableDestinations,
                    onDestinationChange = { viewModel.tripDestination = it },
                    startDate = viewModel.tripStartDate,
                    onStartDateChange = { viewModel.tripStartDate = it },
                    endDate = viewModel.tripEndDate,
                    onEndDateChange = { viewModel.tripEndDate = it },
                    minStartDateMillis = viewModel.getMinStartDateMillis(),
                    minEndDateMillis = viewModel.getMinEndDateMillis(),
                    adults = viewModel.numAdults,
                    onAdultsChange = { viewModel.numAdults = it },
                    children = viewModel.numChildren,
                    onChildrenChange = { viewModel.numChildren = it }
                )
            }

            item {
                HomeActionCard(
                    onCreateTrip = {
                        val route = buildCreateTripRoute(
                            destination = viewModel.tripDestination,
                            startDate = viewModel.tripStartDate,
                            endDate = viewModel.tripEndDate,
                            adults = viewModel.numAdults,
                            children = viewModel.numChildren
                        )
                        onNavigate(route)
                    }
                )
            }

            item {
                HomeSectionHeader(
                    title = "Recomendaciones para ti",
                    actionText = "Ver todo",
                    onAction = { onNavigate("trips") }
                )
                DestinationGrid(recommendedDestinations)
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                HomeSectionHeader(
                    title = "Mejores hoteles",
                    actionText = "Más opciones",
                    onAction = { onNavigate("trips") }
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    topHotels.forEach { hotel ->
                        HotelHighlightCard(hotel = hotel)
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                HomeSectionHeader(
                    title = "Escapadas populares",
                    actionText = "Explorar",
                    onAction = { onNavigate("trips") }
                )
                DestinationGrid(getawayDestinations)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit = {}
) {
    var destination by remember { mutableStateOf("") }
    var checkIn by remember { mutableStateOf("") }
    var checkOut by remember { mutableStateOf("") }

    Surface(
        modifier = modifier.padding(16.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "¿A donde quieres viajar?",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(20.dp))

            ShireTextField(
                value = destination,
                onValueChange = { destination = it },
                label = "Destino",
                placeholder = "País o ciudad",
                leadingIcon = Icons.Default.LocationOn
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ShireTextField(
                    value = checkIn,
                    onValueChange = { checkIn = it },
                    label = "Entrada",
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.DateRange
                )
                ShireTextField(
                    value = checkOut,
                    onValueChange = { checkOut = it },
                    label = "Salida",
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.DateRange
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ShireButton(
                text = "Buscar",
                onClick = { onNavigate("trips") },
                icon = Icons.Default.Search
            )
        }
    }
}

@Composable
fun HomeActionCard(onCreateTrip: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = onCreateTrip,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Crear viaje")
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))
}

private fun buildCreateTripRoute(
    destination: String,
    startDate: String,
    endDate: String,
    adults: Int,
    children: Int
): String {
    if (destination.isBlank()) return "create_trip"

    return "create_trip?destination=${Uri.encode(destination)}" +
        "&startDate=${Uri.encode(startDate)}" +
        "&endDate=${Uri.encode(endDate)}" +
        "&adults=$adults" +
        "&children=$children"
}

@Composable
fun HomeSectionHeader(
    title: String,
    actionText: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SectionTitle(text = title, modifier = Modifier.padding(0.dp))
        TextButton(onClick = onAction) {
            Text(actionText)
        }
    }
}

@Composable
fun HotelHighlightCard(hotel: HotelHighlight) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(14.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = hotel.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = hotel.city,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                AssistChip(
                    onClick = {},
                    label = { Text("${hotel.rating} ★") }
                )
                Text(
                    text = hotel.pricePerNight,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun DestinationGrid(items: List<PopularDestination>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 8.dp)) {
        items.chunked(2).forEach { rowItems ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { item ->
                    DestinationCard(item, Modifier.weight(1f))
                }
            }
        }
    }
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigate = {})
}

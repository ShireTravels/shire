package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shire.ui.components.DestinationCard
import com.example.shire.ui.components.HeaderShire
import com.example.shire.ui.components.SectionTitle
import com.example.shire.ui.components.ShireButton
import com.example.shire.ui.components.ShireTextField

data class PopularDestination(val city: String, val country: String, val imageRes: Int)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {

    val popularDestinations = listOf(
        PopularDestination("Barcelona", "España", 0),
        PopularDestination("Paris", "Francia", 0),
        PopularDestination("Roma", "Italia", 0),
        PopularDestination("Londres", "Reino Unido", 0),
        PopularDestination("Tokio", "Japón", 0),
        PopularDestination("Nueva York", "USA", 0)
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
                HeaderShire(
                    selectedCategory = "Hoteles",
                    onCategoryClick = { categoria ->
                        when (categoria) {
                            "Hoteles" -> onNavigate("home")
                            "Vuelos" -> onNavigate("vuelos")
                            "Alquiler" -> onNavigate("alquiler")
                        }
                    }
                )
            }

            item {
                SearchForm { onNavigate("searchHotel") }
            }

            item {
                SectionTitle(text = "Destinos populares")
            }

            item {
                DestinationGrid(popularDestinations)
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
                text = "¿Dónde quieres alojarte?",
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
                onClick = { onNavigate("searchHotel") },
                icon = Icons.Default.Search
            )
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

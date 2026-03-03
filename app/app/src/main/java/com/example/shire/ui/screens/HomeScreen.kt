package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                .background(Color.White)
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize().background(Color.LightGray)) {
                        Text("Imagen Playa", modifier = Modifier.align(Alignment.Center))
                    }
                }
            }

            item {
                SearchForm(onNavigate = onNavigate)
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
fun SearchForm(modifier: Modifier = Modifier, onNavigate: (String) -> Unit = {}) {
    var destination by remember { mutableStateOf("") }
    var checkIn by remember { mutableStateOf("") }
    var checkOut by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Text("¿Dónde quieres alojarte?", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        ShireTextField(
            value = destination,
            onValueChange = { destination = it },
            label = "Destino",
            placeholder = "País o ciudad",
            leadingIcon = Icons.Default.LocationOn
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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

        Spacer(modifier = Modifier.height(16.dp))

        ShireButton(
            text = "Ver listado de Hoteles",
            onClick = { onNavigate("searchHotel") },
            containerColor = Color(0xFFF1F3F4),
            contentColor = Color.Gray
        )

        ShireButton(
            text = "Buscar",
            onClick = { onNavigate("searchHotel") },
            icon = Icons.Default.Search
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Quick access to new screens as requested
        Text("Explora tus nuevos espacios", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onBackground)
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            ShireButton(
                text = "Mis Viajes",
                onClick = { onNavigate("trips") },
                modifier = Modifier.weight(1f),
                containerColor = Color(0xFFFCE4EC),
                contentColor = Color(0xFFC2185B)
            )
            ShireButton(
                text = "Mi Perfil",
                onClick = { onNavigate("profile") },
                modifier = Modifier.weight(1f),
                containerColor = Color(0xFFE3F2FD),
                contentColor = Color(0xFF1976D2)
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

@Composable
fun DestinationCard(dest: PopularDestination, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp).height(150.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Gray))

            Column(
                modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)
            ) {
                Text(dest.city, color = Color.White, fontWeight = FontWeight.Bold)
                Text(dest.country, color = Color.White, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigate = {})
}

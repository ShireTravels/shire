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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.theme.ShireTheme

data class PopularDestination(val city: String, val country: String, val imageRes: Int)

@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    // 1. Mock Data para destinos
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
            // ITEM 1: Header con navegación
            item {
                HeaderShire(
                    selectedCategory = "Hoteles",
                    onCategoryClick = { categoria ->
                        when (categoria) {
                            "Vuelos" -> onNavigate("vuelos")
                            "Alquiler" -> onNavigate("alquiler")
                        }
                    }
                )
            }

            // ITEM 2: Banner promocional (Mock Image)
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

            // ITEM 3: Formulario de búsqueda (Asegúrate de tener esta función definida)
            item {
                SearchForm()
            }

            // ITEM 4: Título de sección
            item {
                Text(
                    text = "Destinos populares",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // ITEM 5: Cuadrícula de destinos (Mock Data)
            item {
                DestinationGrid(popularDestinations)
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HeaderShire(
    selectedCategory: String,
    onCategoryClick: (String) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Shire",
            color = Color(0xFF0052CC),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryButton("Hoteles", selectedCategory == "Hoteles", Modifier.weight(1f)) {
                onCategoryClick("Hoteles")
            }
            CategoryButton("Vuelos", selectedCategory == "Vuelos", Modifier.weight(1f)) {
                onCategoryClick("Vuelos")
            }
            CategoryButton("Alquiler", selectedCategory == "Alquiler", Modifier.weight(1f)) {
                onCategoryClick("Alquiler")
            }
        }
    }
}

@Composable
fun CategoryButton(
    label: String,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF1A73E8) else Color(0xFFF1F3F4),
            contentColor = if (isSelected) Color.White else Color.Gray
        )
    ) {
        Text(text = label, maxLines = 1, fontSize = 11.sp)
    }
}
@Composable
fun SearchForm(modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp)) {
        Text("¿Dónde quieres alojarte?", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Destino") },
            placeholder = { Text("País o ciudad") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.LocationOn, null) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Entrada") },
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.DateRange, null) }
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Salida") },
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(Icons.Default.DateRange, null) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8))
        ) {
            Icon(Icons.Default.Search, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Buscar")
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

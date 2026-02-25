package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shire.ui.theme.ShireTheme



data class HotelResult(val name: String, val location: String, val rating: String, val price: String)


@Composable
fun HotelsSearch(onNavigate: (String) -> Unit) {
    // 1. Datos simulados específicos para esta pantalla de resultados
    val searchResults = listOf(
        HotelResult("Hotel Plaza", "Barcelona", "4.5/5", "120€"),
        HotelResult("Gran Via Resort", "Madrid", "4.2/5", "95€"),
        HotelResult("Sea View", "Costa Brava", "4.8/5", "150€")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                item {
                    HeaderShire(
                        selectedCategory = "Hoteles",
                        onCategoryClick = { categoria ->
                            // Navegación desde el header
                            if (categoria == "Vuelos") onNavigate("vuelos")
                            if (categoria == "Alquiler") onNavigate("alquiler")
                        }
                    )
                }

                item {
                    // Botón para volver (usando onNavigate)
                    HotelSearchForm(onNavigate)
                }

                // Título de resultados
                item {
                    Text(
                        "Resultados encontrados",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Lista de hoteles "populada"
                items(searchResults.size) { hotel ->
                    HotelResultCard(searchResults[hotel])
                }
            }
        }
    )
}

@Composable
fun HotelSearchForm(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Button(
            onClick = { onNavigate("hoteles") }, // Vuelve a la pantalla principal
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("← Cambiar búsqueda")
        }
    }
}

// Componente para mostrar cada hotel en la lista de resultados
@Composable
fun HotelResultCard(hotel: HotelResult) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(hotel.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            Text(hotel.location, color = Color.Gray)
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("⭐ ${hotel.rating}")
                Text(hotel.price, color = Color(0xFF0052CC), fontWeight = FontWeight.Bold)
            }
        }
    }
}


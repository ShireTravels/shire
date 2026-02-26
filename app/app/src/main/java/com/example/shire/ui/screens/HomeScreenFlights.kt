package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shire.ui.components.HeaderShire
import com.example.shire.ui.components.ShireCard

@Composable
fun HomeScreenFlights(onNavigate: (String) -> Unit) {
    // 1. Datos simulados (Mock Data) específicos para vuelos
    val mockFlights = listOf(
        FlightInfo("Iberia", "09:00", "12:00", "120€"),
        FlightInfo("Vueling", "15:30", "18:45", "85€")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
            ) {
                // ITEM 1: El Header (con lógica de navegación)
                item {
                    HeaderShire(
                        selectedCategory = "Vuelos", // Aquí marcamos "Vuelos" como activo
                        onCategoryClick = { categoria ->
                            when (categoria) {
                                "Hoteles" -> onNavigate("hoteles")
                                "Alquiler" -> onNavigate("alquiler")
                            }
                        }
                    )
                }

                // ITEM 2: El Formulario (lo que antes tenías en FlightScreen)
                item {
                    FlightSearchForm()
                }

                // ITEM 3: Título de ofertas
                item {
                    Text(
                        "Ofertas de vuelos",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                // ITEM 4: La lista de vuelos (Mock Data)
                items(mockFlights) { flight ->
                    ShireCard(
                        title = flight.airline,
                        subtitle = "${flight.departure} - ${flight.arrival}",
                        price = flight.price
                    )
                }
            }
        }
    )
}

// Modelo de datos para el simulacro
data class FlightInfo(val airline: String, val departure: String, val arrival: String, val price: String)

@Composable
fun FlightSearchForm() {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Origen") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Destino") }, modifier = Modifier.fillMaxWidth())
        // ... campos de fechas ...
        Button(onClick = {}, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Text("Buscar vuelos")
        }
    }
}

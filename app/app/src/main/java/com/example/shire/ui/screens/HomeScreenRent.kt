package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.shire.ui.components.HeaderShire
import com.example.shire.ui.components.ShireButton
import com.example.shire.ui.components.ShireCard

// 1. Modelo de datos para los coches (Mock Data)
data class CarRental(val model: String, val type: String, val pricePerDay: String)

@Composable
fun HomeScreenRent(onNavigate: (String) -> Unit) {
    // Datos simulados para "poblar" la pantalla
    val mockCars = listOf(
        CarRental("Toyota Rav4", "SUV - Automático", "55€"),
        CarRental("Fiat 500", "Económico - Manual", "25€"),
        CarRental("Tesla Model 3", "Lujo - Eléctrico", "89€")
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
                // ITEM 1: Header con navegación corregida
                item {
                    HeaderShire(
                        selectedCategory = "Alquiler", // Marca este botón en azul
                        onCategoryClick = { categoria ->
                            when (categoria) {
                                "Hoteles" -> onNavigate("hoteles")
                                "Vuelos" -> onNavigate("vuelos")
                            }
                        }
                    )
                }

                // ITEM 2: Formulario de búsqueda de coches
                item {
                    RentSearchForm()
                }

                // ITEM 3: Título de sección
                item {
                    Text(
                        "Coches disponibles cerca de ti",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // ITEM 4: Listado de coches (Populated with Mock Data)
                items(mockCars) { car ->
                    ShireCard(
                        title = car.model,
                        subtitle = car.type,
                        price = car.pricePerDay,
                        priceSuffix = " / día",
                        icon = {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = Color.LightGray
                            )
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun RentSearchForm() {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Lugar de recogida") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Aeropuerto o Ciudad") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Recogida") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text("Entrega") },
                modifier = Modifier.weight(1f)
            )
        }

        ShireButton(
            text = "Buscar disponibilidad",
            onClick = {},
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

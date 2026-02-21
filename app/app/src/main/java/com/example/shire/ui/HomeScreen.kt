package com.example.shire.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class HomeScreen {
    // 1. Definimos el modelo de datos (MOCK MODEL)
    data class Destination(
        val id: Int,
        val name: String,
        val location: String,
        val pricePerNight: Double
    )

    // 2. Creamos la lista de datos simulados (MOCKED DATA)
    val mockDestinations = listOf(
        Destination(1, "Hotel Paraíso Azul", "Islas Maldivas", 250.0),
        Destination(2, "Refugio de Montaña", "Suiza", 180.0),
        Destination(3, "Posada del Sol", "Málaga, España", 95.0),
        Destination(4, "Urban Loft", "New York, USA", 310.0)
    )

    // 3. La pantalla principal que consume esos datos
    @Composable
    fun HomeScreen() {
        Scaffold(
            topBar = {
                Text(
                    "Explorar Destinos",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        ) { paddingValues ->
            // LazyColumn es el equivalente al RecyclerView (una lista eficiente)
            LazyColumn(modifier = Modifier.padding(paddingValues)) {
                items(mockDestinations) { destination ->
                    DestinationCard(destination)
                }
            }
        }
    }

    // 4. El diseño de cada tarjeta (Item)
    @Composable
    fun DestinationCard(dest: Destination) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = dest.name, style = MaterialTheme.typography.titleLarge)
                Text(text = dest.location, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${dest.pricePerNight}€ / noche",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // 5. PREVIEW: Esto permite ver la pantalla poblada en Android Studio sin usar el móvil
    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun HomeScreenPreview() {
        HomeScreen()
    }

}
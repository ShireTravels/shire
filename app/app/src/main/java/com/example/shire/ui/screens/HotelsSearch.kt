package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.shire.ui.components.HeaderShire
import com.example.shire.ui.components.SectionTitle
import com.example.shire.ui.components.ShireButton
import com.example.shire.ui.components.ShireCard

data class HotelResult(val name: String, val location: String, val rating: String, val price: String)

@Composable
fun HotelsSearch(onNavigate: (String) -> Unit) {
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
                            if (categoria == "Vuelos") onNavigate("vuelos")
                            if (categoria == "Alquiler") onNavigate("alquiler")
                        }
                    )
                }

                item {
                    HotelSearchForm(onNavigate)
                }

                item {
                    SectionTitle(text = "Resultados encontrados")
                }

                items(searchResults.size) { index ->
                    val hotel = searchResults[index]
                    ShireCard(
                        title = hotel.name,
                        subtitle = hotel.location,
                        price = hotel.price,
                        annotation = "⭐ ${hotel.rating}"
                    )
                }
            }
        }
    )
}

@Composable
fun HotelSearchForm(onNavigate: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ShireButton(
            text = "← Cambiar búsqueda",
            onClick = { onNavigate("hoteles") },
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

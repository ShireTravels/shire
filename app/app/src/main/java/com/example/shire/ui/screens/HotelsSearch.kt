package com.example.shire.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shire.ui.components.SectionTitle
import com.example.shire.ui.components.ShireButton
import com.example.shire.ui.components.ShireTextField
import com.example.shire.ui.theme.ShireTheme

data class HotelOffer(val city: String, val price: String)
data class HotelResult(val name: String, val location: String, val rating: String, val price: String)

@Composable
fun HotelsSearch(onNavigate: (String) -> Unit) {
    var destino by remember { mutableStateOf("") }
    var fechas by remember { mutableStateOf("") }
    var viajeros by remember { mutableStateOf("") }
    var alojamientoSoloParte by remember { mutableStateOf(false) }

    val offers = listOf(
        HotelOffer("Brighton", "165"),
        HotelOffer("Harlow", "120"),
        HotelOffer("Londres", "85"),
        HotelOffer("Paris", "140")
    )

    val searchResults = listOf(
        HotelResult("Hotel Plaza", "Barcelona", "4.5/5", "120€"),
        HotelResult("Gran Via Resort", "Madrid", "4.2/5", "95€"),
        HotelResult("Sea View", "Costa Brava", "4.8/5", "150€")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Content
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = "Reserva de Hotel",
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Form Content
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ShireTextField(
                            label = "Destino/nombre del hotel",
                            value = destino,
                            onValueChange = { destino = it },
                            placeholder = "¿Adónde quieres ir?"
                        )
                        
                        ShireTextField(
                            label = "¿Cuándo?",
                            value = fechas,
                            onValueChange = { fechas = it },
                            placeholder = "4 mar – 7 mar"
                        )
                        
                        ShireTextField(
                            label = "Viajeros/Habitaciones",
                            value = viajeros,
                            onValueChange = { viajeros = it },
                            placeholder = "2 viajeros • 1 habitación"
                        )
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Checkbox(
                                checked = alojamientoSoloParte,
                                onCheckedChange = { alojamientoSoloParte = it }
                            )
                            Text(
                                text = "Alojamiento solo para una parte de mi viaje",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        ShireButton(
                            text = "Buscar",
                            onClick = { /* TODO implement search action */ },
                            icon = Icons.Default.Search
                        )
                    }
                }
            }

            // Offers Section Header
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text(
                        text = "Ofertas de Hotel",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Descubre nuestros destinos más populares",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Offers List
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(offers) { offer ->
                        HotelOfferCard(offer = offer)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                SectionTitle(text = "Resultados encontrados")
            }

            items(searchResults) { hotel ->
                HotelResultCard(hotel = hotel, onClick = { onNavigate("hotel_details") })
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun HotelOfferCard(offer: HotelOffer, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(200.dp)
            .height(240.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            )
            
            // City Name
            Text(
                text = offer.city,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp)
            )
            
            // Price Tag
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(Color(0xFF006CE4), RoundedCornerShape(4.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "desde ${offer.price} €",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun HotelResultCard(hotel: HotelResult, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(140.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(120.dp)
                    .background(Color.Gray)
            )

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Row: Title, Location and Rating
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hotel.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = hotel.location,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Rating Badge
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF003B95), // Booking.com style blue
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = hotel.rating,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                // Bottom Row: Price Strategy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "1 noche, 2 adultos",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = hotel.price,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HotelsSearchPreview() {
    ShireTheme {
        HotelsSearch(onNavigate = {})
    }
}

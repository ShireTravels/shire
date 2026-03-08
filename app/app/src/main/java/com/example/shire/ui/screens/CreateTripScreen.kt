package com.example.shire.ui.screens


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shire.ui.components.SectionTitle
import com.example.shire.ui.components.ShireButton
import com.example.shire.ui.components.ShireTextField
import com.example.shire.ui.components.StepProgressBar
import com.example.shire.ui.theme.ShireTheme

// ──────────────────────────────── Data Models ────────────────────────────────

data class MockHotel(
    val name: String, val location: String, val rating: String,
    val price: String, val description: String, val amenities: List<String>
)

data class MockFlight(
    val airline: String, val route: String, val departure: String,
    val arrival: String, val price: String, val duration: String, val stops: String
)

data class MockCar(
    val model: String, val type: String, val pricePerDay: String,
    val transmission: String, val seats: String, val features: List<String>
)

data class MockSpot(
    val name: String, val category: String, val rating: String,
    val description: String, val hours: String, val price: String
)

// ──────────────────────────────── Mock Data ──────────────────────────────────

private val mockHotels = listOf(
    MockHotel("Hotel Plaza", "Barcelona, España", "4.5/5", "120€/noche",
        "Hotel de lujo situado en el corazón de Barcelona con vistas al mar Mediterráneo.",
        listOf("Wi-Fi", "Piscina", "Spa", "Restaurante", "Parking")),
    MockHotel("Gran Vía Resort", "Madrid, España", "4.2/5", "95€/noche",
        "Resort moderno en plena Gran Vía con acceso a las mejores tiendas y teatros.",
        listOf("Wi-Fi", "Gimnasio", "Bar en la azotea", "Room Service")),
    MockHotel("Sea View Inn", "Costa Brava, España", "4.8/5", "150€/noche",
        "Hotel boutique frente al mar con habitaciones panorámicas y playa privada.",
        listOf("Wi-Fi", "Playa privada", "Restaurante", "Kayak", "Snorkel")),
    MockHotel("Montaña Lodge", "Pirineos, España", "4.6/5", "110€/noche",
        "Lodge acogedor en los Pirineos, ideal para senderismo y actividades de montaña.",
        listOf("Wi-Fi", "Chimenea", "Restaurante", "Rutas guiadas"))
)

private val mockFlights = listOf(
    MockFlight("Iberia", "BCN → TYO", "09:00", "22:30", "420€",
        "13h 30min", "1 escala (Frankfurt)"),
    MockFlight("Vueling", "MAD → BCN", "15:30", "16:45", "85€",
        "1h 15min", "Directo"),
    MockFlight("Ryanair", "BCN → ROM", "07:00", "09:15", "45€",
        "2h 15min", "Directo"),
    MockFlight("Air Europa", "BCN → NYC", "11:00", "15:30", "380€",
        "8h 30min", "Directo")
)

private val mockCars = listOf(
    MockCar("Toyota RAV4", "SUV", "55€/día",
        "Automático", "5 plazas", listOf("GPS", "Bluetooth", "Cámara trasera", "A/C")),
    MockCar("Fiat 500", "Económico", "25€/día",
        "Manual", "4 plazas", listOf("Bluetooth", "USB", "A/C")),
    MockCar("Tesla Model 3", "Eléctrico Premium", "89€/día",
        "Automático", "5 plazas", listOf("Autopilot", "GPS", "Carga rápida", "Premium Audio")),
    MockCar("VW Transporter", "Furgoneta", "70€/día",
        "Manual", "9 plazas", listOf("GPS", "A/C", "Gran maletero"))
)

private val mockSpots = listOf(
    MockSpot("Templo Senso-ji", "Templo", "4.7/5",
        "El templo budista más antiguo de Tokio, situado en Asakusa. Incluye la famosa puerta Kaminarimon.",
        "06:00 - 17:00", "Gratis"),
    MockSpot("Torre Eiffel", "Monumento", "4.6/5",
        "Icono de París con vistas panorámicas de la ciudad desde sus tres niveles.",
        "09:30 - 23:45", "26€"),
    MockSpot("Sagrada Familia", "Basílica", "4.8/5",
        "Obra maestra de Gaudí en Barcelona, patrimonio de la humanidad.",
        "09:00 - 20:00", "26€"),
    MockSpot("Mercado de la Boquería", "Mercado", "4.5/5",
        "El mercado gastronómico más famoso de Barcelona con productos frescos y tapas.",
        "08:00 - 20:30", "Gratis")
)

// ──────────────────────────────── Main Screen ────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    onNavigateUp: () -> Unit,
    onNavigate: (String) -> Unit
) {
    var currentStep by remember { mutableIntStateOf(0) }
    val stepLabels = listOf("Destino", "Hotel", "Vuelo", "Coche", "Lugares")

    // --- Destination step state ---
    var tripDestination by remember { mutableStateOf("") }
    var tripStartDate by remember { mutableStateOf("") }
    var tripEndDate by remember { mutableStateOf("") }
    var numAdults by remember { mutableIntStateOf(2) }
    var numChildren by remember { mutableIntStateOf(0) }

    // Detail dialog state
    var selectedItem by remember { mutableStateOf<Any?>(null) }

    // Detail dialog
    if (selectedItem != null) {
        AlertDialog(
            onDismissRequest = { selectedItem = null },
            confirmButton = {
                TextButton(onClick = { selectedItem = null }) {
                    Text("Cerrar")
                }
            },
            title = {
                Text(
                    text = when (val item = selectedItem) {
                        is MockHotel -> item.name
                        is MockFlight -> "${item.airline} · ${item.route}"
                        is MockCar -> item.model
                        is MockSpot -> item.name
                        else -> ""
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    when (val item = selectedItem) {
                        is MockHotel -> HotelDetailContent(item)
                        is MockFlight -> FlightDetailContent(item)
                        is MockCar -> CarDetailContent(item)
                        is MockSpot -> SpotDetailContent(item)
                    }
                }
            },
            shape = RoundedCornerShape(24.dp)
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Crear Viaje",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progress Bar
            item {
                StepProgressBar(currentStep = currentStep, steps = stepLabels)
            }

            // Planning date info banner (shown on steps after Destino)
            if (currentStep > 0 && (tripDestination.isNotBlank() || tripStartDate.isNotBlank())) {
                item {
                    TripInfoBanner(
                        destination = tripDestination,
                        dates = if (tripStartDate.isNotBlank() && tripEndDate.isNotBlank())
                            "$tripStartDate → $tripEndDate" else tripStartDate,
                        travelers = "$numAdults adultos" +
                                if (numChildren > 0) " · $numChildren niños" else ""
                    )
                }
            }

            // Step title
            item {
                SectionTitle(
                    text = when (currentStep) {
                        0 -> "¿Dónde quieres ir?"
                        1 -> "Reserva de Hotel"
                        2 -> "Reserva de Vuelo"
                        3 -> "Alquiler de Coche"
                        4 -> "Lugares a Visitar"
                        else -> ""
                    }
                )
            }

            // Step content
            when (currentStep) {
                0 -> {
                    item {
                        DestinationStepContent(
                            destination = tripDestination,
                            onDestinationChange = { tripDestination = it },
                            startDate = tripStartDate,
                            onStartDateChange = { tripStartDate = it },
                            endDate = tripEndDate,
                            onEndDateChange = { tripEndDate = it },
                            adults = numAdults,
                            onAdultsChange = { numAdults = it },
                            children = numChildren,
                            onChildrenChange = { numChildren = it }
                        )
                    }
                }
                1 -> {
                    item { StepSubtitle("Hoteles disponibles") }
                    items(mockHotels) { hotel ->
                        BrowseableCard(
                            icon = Icons.Default.Hotel,
                            iconTint = Color(0xFFD84315),
                            iconBg = Color(0xFFFBE9E7),
                            title = hotel.name,
                            subtitle = hotel.location,
                            trailing = hotel.price,
                            badge = hotel.rating,
                            onClick = { onNavigate("hotel_details") }
                        )
                    }
                }
                2 -> {
                    item { StepSubtitle("Vuelos disponibles") }
                    items(mockFlights) { flight ->
                        BrowseableCard(
                            icon = Icons.Default.Flight,
                            iconTint = Color(0xFF1976D2),
                            iconBg = Color(0xFFE3F2FD),
                            title = "${flight.airline} · ${flight.route}",
                            subtitle = "${flight.departure} → ${flight.arrival} · ${flight.duration}",
                            trailing = flight.price,
                            badge = flight.stops,
                            onClick = { null }
                        )
                    }
                }
                3 -> {
                    item { StepSubtitle("Coches disponibles") }
                    items(mockCars) { car ->
                        BrowseableCard(
                            icon = Icons.Default.DirectionsCar,
                            iconTint = Color(0xFF388E3C),
                            iconBg = Color(0xFFE8F5E9),
                            title = car.model,
                            subtitle = "${car.type} · ${car.transmission}",
                            trailing = car.pricePerDay,
                            badge = car.seats,
                            onClick = { null }
                        )
                    }
                }
                4 -> {
                    item { StepSubtitle("Lugares para visitar") }
                    items(mockSpots) { spot ->
                        BrowseableCard(
                            icon = Icons.Default.Place,
                            iconTint = Color(0xFFC2185B),
                            iconBg = Color(0xFFFCE4EC),
                            title = spot.name,
                            subtitle = "${spot.category} · ${spot.hours}",
                            trailing = spot.price,
                            badge = spot.rating,
                            onClick = { null }
                        )
                    }
                }
            }

            // Navigation buttons
            item {
                Spacer(modifier = Modifier.height(16.dp))
                NavigationButtons(
                    currentStep = currentStep,
                    totalSteps = stepLabels.size,
                    onPrevious = { if (currentStep > 0) currentStep-- },
                    onNext = { if (currentStep < stepLabels.size - 1) currentStep++ },
                    onSkip = { if (currentStep < stepLabels.size - 1) currentStep++ },
                    onAddDestination = { currentStep = 0 },
                    onFinish = { onNavigate("trip_details/1") }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ─────────────────────────── Browseable Card ─────────────────────────────────

@Composable
private fun BrowseableCard(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    trailing: String,
    badge: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconBg
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Title + subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Price
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = trailing,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "Ver detalles",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// ──────────────────────── Detail Bottom Sheet Contents ────────────────────────

@Composable
private fun ColumnScope.HotelDetailContent(hotel: MockHotel) {
    DetailHeader(Icons.Default.Hotel, Color(0xFFD84315), Color(0xFFFBE9E7), hotel.name)
    DetailRow("Ubicación", hotel.location)
    DetailRow("Valoración", hotel.rating)
    DetailRow("Precio", hotel.price)
    Spacer(modifier = Modifier.height(12.dp))
    Text(hotel.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Servicios", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    ChipRow(hotel.amenities)
}

@Composable
private fun ColumnScope.FlightDetailContent(flight: MockFlight) {
    DetailHeader(Icons.Default.Flight, Color(0xFF1976D2), Color(0xFFE3F2FD), "${flight.airline} · ${flight.route}")
    DetailRow("Salida", flight.departure)
    DetailRow("Llegada", flight.arrival)
    DetailRow("Duración", flight.duration)
    DetailRow("Escalas", flight.stops)
    DetailRow("Precio", flight.price)
}

@Composable
private fun ColumnScope.CarDetailContent(car: MockCar) {
    DetailHeader(Icons.Default.DirectionsCar, Color(0xFF388E3C), Color(0xFFE8F5E9), car.model)
    DetailRow("Tipo", car.type)
    DetailRow("Transmisión", car.transmission)
    DetailRow("Plazas", car.seats)
    DetailRow("Precio", car.pricePerDay)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Características", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    ChipRow(car.features)
}

@Composable
private fun ColumnScope.SpotDetailContent(spot: MockSpot) {
    DetailHeader(Icons.Default.Place, Color(0xFFC2185B), Color(0xFFFCE4EC), spot.name)
    DetailRow("Categoría", spot.category)
    DetailRow("Valoración", spot.rating)
    DetailRow("Horario", spot.hours)
    DetailRow("Precio", spot.price)
    Spacer(modifier = Modifier.height(12.dp))
    Text(spot.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
}

// ────────────────────────── Detail Helper Composables ─────────────────────────

@Composable
private fun DetailHeader(icon: ImageVector, tint: Color, bg: Color, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 16.dp)) {
        Surface(modifier = Modifier.size(52.dp), shape = RoundedCornerShape(14.dp), color = bg) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(28.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun ChipRow(items: List<String>) {
    @OptIn(ExperimentalLayoutApi::class)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            SuggestionChip(
                onClick = {},
                label = { Text(item, style = MaterialTheme.typography.labelMedium) },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

// ─────────────────────── Destination Step Content ────────────────────────────

@Composable
private fun DestinationStepContent(
    destination: String, onDestinationChange: (String) -> Unit,
    startDate: String, onStartDateChange: (String) -> Unit,
    endDate: String, onEndDateChange: (String) -> Unit,
    adults: Int, onAdultsChange: (Int) -> Unit,
    children: Int, onChildrenChange: (Int) -> Unit
) {

    // Estados para controlar la visibilidad de los calendarios
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ShireTextField(
                value = destination,
                onValueChange = onDestinationChange,
                label = "Destino",
                placeholder = "País, ciudad o región",
                leadingIcon = Icons.Default.Search
            )

            Text(
                text = "Fechas del viaje",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ShireTextField(
                    value = startDate,
                    onValueChange = {},
                    label = "Ida",
                    placeholder = "dd/mm/aaaa",
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.DateRange,
                    enabled = false, // Evita que se abra el teclado
                    onClick = { showStartDatePicker = true } // Abre el calendario
                )
                ShireTextField(
                    value = endDate,
                    onValueChange = { },
                    label = "Vuelta",
                    placeholder = "dd/mm/aaaa",
                    modifier = Modifier.weight(1f),
                    leadingIcon = Icons.Default.DateRange,
                    enabled = false, // Evita que se abra el teclado
                    onClick = { showEndDatePicker = true } // Abre el calendario
                )

                // --- LÓGICA DE LOS DIÁLOGOS DE FECHAS ---

                if (showStartDatePicker) {
                    ShireDatePickerDialog(
                        onDateSelected = { fecha ->
                            onStartDateChange(fecha) // Actualiza el estado de la fecha de ida
                            showStartDatePicker = false // Cierra el calendario
                        },
                        onDismiss = { showStartDatePicker = false }
                    )
                }

                if (showEndDatePicker) {
                    ShireDatePickerDialog(
                        onDateSelected = { fecha ->
                            onEndDateChange(fecha) // Actualiza el estado de la fecha de vuelta
                            showEndDatePicker = false // Cierra el calendario
                        },
                        onDismiss = { showEndDatePicker = false }
                    )
                }
            }



            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                text = "Viajeros",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            TravelerCounter(
                label = "Adultos",
                subtitle = "Mayores de 12 años",
                count = adults,
                onCountChange = onAdultsChange,
                minCount = 1
            )
            TravelerCounter(
                label = "Niños",
                subtitle = "De 0 a 12 años",
                count = children,
                onCountChange = onChildrenChange,
                minCount = 0
            )
        }
    }
}

@Composable
private fun TravelerCounter(
    label: String,
    subtitle: String,
    count: Int,
    onCountChange: (Int) -> Unit,
    minCount: Int = 0
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilledIconButton(
                onClick = { if (count > minCount) onCountChange(count - 1) },
                enabled = count > minCount,
                modifier = Modifier.size(36.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.Default.Remove, contentDescription = "Menos", modifier = Modifier.size(18.dp))
            }
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            FilledIconButton(
                onClick = { onCountChange(count + 1) },
                modifier = Modifier.size(36.dp),
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.Default.Add, contentDescription = "Más", modifier = Modifier.size(18.dp))
            }
        }
    }
}

// ────────────────────────── Trip Info Banner ──────────────────────────────────

@Composable
private fun TripInfoBanner(
    destination: String,
    dates: String,
    travelers: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.TravelExplore,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                if (destination.isNotBlank()) {
                    Text(
                        text = destination,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Row {
                    if (dates.isNotBlank()) {
                        Text(
                            text = dates,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    if (dates.isNotBlank() && travelers.isNotBlank()) {
                        Text(
                            text = " · ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = travelers,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// ─────────────────────────── Step Subtitle ────────────────────────────────────

@Composable
private fun StepSubtitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

// ─────────────────────────── Navigation Buttons ─────────────────────────────

@Composable
private fun NavigationButtons(
    currentStep: Int,
    totalSteps: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onAddDestination: () -> Unit,
    onFinish: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (currentStep > 0) {
                OutlinedButton(
                    onClick = onPrevious,
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Anterior", style = MaterialTheme.typography.labelLarge)
                }
            }

            if (currentStep < totalSteps - 1) {
                ShireButton(
                    text = "Siguiente",
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    icon = Icons.AutoMirrored.Filled.ArrowForward
                )
            } else {
                ShireButton(
                    text = "Finalizar",
                    onClick = onFinish,
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Check
                )
            }
        }

        // Last step: show "Añadir destino" to loop back to step 1
        if (currentStep == totalSteps - 1) {
            OutlinedButton(
                onClick = onAddDestination,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Añadir otro destino", style = MaterialTheme.typography.labelLarge)
            }
        }

        if (currentStep < totalSteps - 1) {
            TextButton(
                onClick = onSkip,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Omitir este paso",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShireDatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis?.let {
        val date = java.util.Date(it)
        val format = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        format.format(date)
    } ?: ""

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateTripScreenPreview() {
    ShireTheme {
        CreateTripScreen(onNavigateUp = {}, onNavigate = {})
    }
}

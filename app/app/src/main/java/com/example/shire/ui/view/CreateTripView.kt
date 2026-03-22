package com.example.shire.ui.view


import androidx.compose.foundation.BorderStroke
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shire.ui.components.SectionTitle
import com.example.shire.ui.components.ShireButton
import com.example.shire.ui.components.ShireTextField
import com.example.shire.ui.components.StepProgressBar
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.CreateTripViewModel
import com.example.shire.ui.components.DestinationStepContent


// ──────────────────────────────── Main Screen ────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    onNavigateUp: () -> Unit,
    onNavigate: (String) -> Unit,
    initialDestination: String? = null,
    initialStartDate: String? = null,
    initialEndDate: String? = null,
    initialAdults: Int = 2,
    initialChildren: Int = 0,
    viewModel: CreateTripViewModel = hiltViewModel()
) {
    var currentStep by remember(initialDestination) {
        mutableIntStateOf(if (initialDestination.isNullOrBlank()) 0 else 1)
    }
    val stepLabels = listOf("Destino", "Hotel", "Vuelo", "Coche", "Lugares")

    LaunchedEffect(initialDestination, initialStartDate, initialEndDate, initialAdults, initialChildren) {
        if (!initialDestination.isNullOrBlank()) {
            viewModel.tripDestination = initialDestination
            viewModel.tripStartDate = initialStartDate.orEmpty()
            viewModel.tripEndDate = initialEndDate.orEmpty()
            viewModel.numAdults = initialAdults
            viewModel.numChildren = initialChildren
            currentStep = 1
        }
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
            if (currentStep > 0 && (viewModel.tripDestination.isNotBlank() || viewModel.tripStartDate.isNotBlank())) {
                item {
                    TripInfoBanner(
                        destination = viewModel.tripDestination,
                        dates = if (viewModel.tripStartDate.isNotBlank() && viewModel.tripEndDate.isNotBlank())
                            "${viewModel.tripStartDate} → ${viewModel.tripEndDate}" else viewModel.tripStartDate,
                        travelers = "${viewModel.numAdults} adultos" +
                                if (viewModel.numChildren > 0) " · ${viewModel.numChildren} niños" else ""
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
                            destination = viewModel.tripDestination,
                            availableDestinations = viewModel.availableDestinations,
                            onDestinationChange = { viewModel.tripDestination = it },
                            startDate = viewModel.tripStartDate,
                            onStartDateChange = { viewModel.tripStartDate = it },
                            endDate = viewModel.tripEndDate,
                            onEndDateChange = { viewModel.tripEndDate = it },
                            minStartDateMillis = viewModel.getMinStartDateMillis(),
                            minEndDateMillis = viewModel.getMinEndDateMillis(),
                            adults = viewModel.numAdults,
                            onAdultsChange = { viewModel.numAdults = it },
                            children = viewModel.numChildren,
                            onChildrenChange = { viewModel.numChildren = it }
                        )
                    }
                }
                1 -> {
                    item { StepSubtitle("Hoteles disponibles") }
                    items(viewModel.hotels) { hotel ->
                        SelectableCard(
                            icon = Icons.Default.Hotel,
                            iconTint = Color(0xFFD84315),
                            iconBg = Color(0xFFFBE9E7),
                            title = hotel.name,
                            subtitle = "${hotel.location} · ${hotel.rating}⭐",
                            trailing = "${hotel.price}€/nit",
                            badge = hotel.amenities.take(3).joinToString(", "),
                            isSelected = viewModel.selectedHotel == hotel,
                            onClick = {
                                viewModel.selectedHotel = if (viewModel.selectedHotel == hotel) null else hotel
                            }
                        )
                    }
                }
                2 -> {
                    item { StepSubtitle("Vuelos disponibles") }
                    items(viewModel.flights) { flight ->
                        SelectableCard(
                            icon = Icons.Default.Flight,
                            iconTint = Color(0xFF1976D2),
                            iconBg = Color(0xFFE3F2FD),
                            title = "${flight.company} · ${flight.flightNumber}",
                            subtitle = "${flight.departureCity} → ${flight.arrivalCity} · Terminal ${flight.terminal}",
                            trailing = "${flight.price}€",
                            badge = flight.type,
                            isSelected = viewModel.selectedFlight == flight,
                            onClick = {
                                viewModel.selectedFlight = if (viewModel.selectedFlight == flight) null else flight
                            }
                        )
                    }
                }
                3 -> {
                    item { StepSubtitle("Coches disponibles") }
                    items(viewModel.cars) { car ->
                        SelectableCard(
                            icon = Icons.Default.DirectionsCar,
                            iconTint = Color(0xFF388E3C),
                            iconBg = Color(0xFFE8F5E9),
                            title = car.model,
                            subtitle = "${car.type} · ${car.transmission}",
                            trailing = "${car.pricePerDay}€/día",
                            badge = "${car.seats} plazas",
                            isSelected = viewModel.selectedCar == car,
                            onClick = {
                                viewModel.selectedCar = if (viewModel.selectedCar == car) null else car
                            }
                        )
                    }
                }
                4 -> {
                    item { StepSubtitle("Lugares para visitar") }
                    items(viewModel.places) { place ->
                        val isSelected = viewModel.selectedPlaces.containsKey(place)
                        Column {
                            SelectableCard(
                                icon = Icons.Default.Place,
                                iconTint = Color(0xFFC2185B),
                                iconBg = Color(0xFFFCE4EC),
                                title = place.name,
                                subtitle = "${place.location} · ${place.type}",
                                trailing = if (place.price == 0.0) "Gratis" else "${place.price}€",
                                badge = "${place.rating}⭐",
                                isSelected = isSelected,
                                onClick = { viewModel.togglePlaceSelection(place) }
                            )

                            if (isSelected) {
                                val tripDays = viewModel.computeTripDays()
                                val selectedDay = viewModel.selectedPlaces[place] ?: 1

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 32.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Día de visita",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        FilledIconButton(
                                            onClick = { if (selectedDay > 1) viewModel.updatePlaceDay(place, selectedDay - 1) },
                                            enabled = selectedDay > 1,
                                            modifier = Modifier.size(32.dp),
                                            colors = IconButtonDefaults.filledIconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        ) {
                                            Icon(Icons.Default.Remove, contentDescription = "Día anterior", modifier = Modifier.size(16.dp))
                                        }
                                        Text(
                                            text = "DÍA $selectedDay",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 12.dp)
                                        )
                                        FilledIconButton(
                                            onClick = { if (selectedDay < tripDays) viewModel.updatePlaceDay(place, selectedDay + 1) },
                                            enabled = selectedDay < tripDays,
                                            modifier = Modifier.size(32.dp),
                                            colors = IconButtonDefaults.filledIconButtonColors(
                                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        ) {
                                            Icon(Icons.Default.Add, contentDescription = "Día siguiente", modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
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
                    onAddDestination = {
                        viewModel.addCurrentDestination()
                        currentStep = 0
                    },
                    onFinish = {
                        val newId = viewModel.createTrip()
                        onNavigate("trip_details/$newId")
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// ─────────────────────────── Selectable Card ─────────────────────────────────

@Composable
private fun SelectableCard(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    trailing: String,
    badge: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            else
                MaterialTheme.colorScheme.surface
        ),
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
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

            // Price + selection indicator
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = trailing,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (isSelected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Seleccionado",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                } else {
                    Icon(
                        Icons.Default.RadioButtonUnchecked,
                        contentDescription = "No seleccionado",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateTripScreenPreview() {
    ShireTheme {
        CreateTripScreen(onNavigateUp = {}, onNavigate = {})
    }
}

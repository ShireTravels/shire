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
import com.example.shire.ui.components.ShireDatePickerDialog
import com.example.shire.ui.components.StepProgressBar
import com.example.shire.ui.components.ShireTimePickerDialog
import androidx.compose.ui.res.stringResource
import com.example.shire.R
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.CreateTripViewModel
import com.example.shire.ui.components.DestinationStepContent
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


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
    val stepLabels = listOf(
        stringResource(id = R.string.step_destination),
        stringResource(id = R.string.step_hotel),
        stringResource(id = R.string.step_flight),
        stringResource(id = R.string.step_car),
        stringResource(id = R.string.step_activities)
    )
    var showAddActivityDialog by remember { mutableStateOf(false) }

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

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.errorMessage = null
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.create_trip),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(id = R.string.back_desc))
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
                        travelers = stringResource(id = R.string.adults_format, viewModel.numAdults) +
                                if (viewModel.numChildren > 0) stringResource(id = R.string.children_format_dot, viewModel.numChildren) else ""
                    )
                }
            }

            // Step title
            item {
                SectionTitle(
                    text = when (currentStep) {
                        0 -> stringResource(id = R.string.where_to_go)
                        1 -> stringResource(id = R.string.hotel_reservation)
                        2 -> stringResource(id = R.string.flight_reservation)
                        3 -> stringResource(id = R.string.car_rental)
                        4 -> stringResource(id = R.string.add_activities)
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
                    item { StepSubtitle(stringResource(id = R.string.available_hotels)) }
                    items(viewModel.hotels) { hotel ->
                        SelectableCard(
                            icon = Icons.Default.Hotel,
                            iconTint = Color(0xFFD84315),
                            iconBg = Color(0xFFFBE9E7),
                            title = hotel.name,
                            subtitle = "${hotel.location} · ${hotel.rating}⭐",
                            trailing = "${hotel.price}${stringResource(id = R.string.price_per_night)}",
                            badge = hotel.amenities.take(3).joinToString(", "),
                            isSelected = viewModel.selectedHotel == hotel,
                            onClick = {
                                viewModel.selectedHotel = if (viewModel.selectedHotel == hotel) null else hotel
                            }
                        )
                    }
                }
                2 -> {
                    item { StepSubtitle(stringResource(id = R.string.available_flights)) }
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
                    item { StepSubtitle(stringResource(id = R.string.available_cars)) }
                    items(viewModel.cars) { car ->
                        SelectableCard(
                            icon = Icons.Default.DirectionsCar,
                            iconTint = Color(0xFF388E3C),
                            iconBg = Color(0xFFE8F5E9),
                            title = car.model,
                            subtitle = "${car.type} · ${car.transmission}",
                            trailing = "${car.pricePerDay}${stringResource(id = R.string.price_per_day)}",
                            badge = stringResource(id = R.string.seats_format, car.seats),
                            isSelected = viewModel.selectedCar == car,
                            onClick = {
                                viewModel.selectedCar = if (viewModel.selectedCar == car) null else car
                            }
                        )
                    }
                }
                4 -> {
                    item { StepSubtitle(stringResource(id = R.string.trip_activities)) }
                    items(viewModel.pendingActivities) { activity ->
                        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.weight(1f)) {
                                SelectableCard(
                                    icon = Icons.Default.Event,
                                    iconTint = Color(0xFF9C27B0),
                                    iconBg = Color(0xFFF3E5F5),
                                    title = activity.title,
                                    subtitle = "${activity.date} · ${activity.time}",
                                    trailing = if (activity.price == 0.0) stringResource(id = R.string.free) else "${activity.price}€",
                                    badge = "",
                                    isSelected = false,
                                    onClick = { }
                                )
                            }
                            IconButton(onClick = { viewModel.removePendingActivity(activity) }) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete_desc), tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedButton(
                            onClick = { showAddActivityDialog = true },
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(stringResource(id = R.string.add_new_activity), style = MaterialTheme.typography.labelLarge)
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
                    onNext = { 
                        if (currentStep == 0) {
                            if (viewModel.validateDestinationStep()) {
                                currentStep++
                            }
                        } else if (currentStep < stepLabels.size - 1) {
                            currentStep++
                        }
                    },
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

    if (showAddActivityDialog) {
        var newTitle by remember { mutableStateOf("") }
        var newDesc by remember { mutableStateOf("") }
        var newDate by remember { mutableStateOf("") }
        var newTime by remember { mutableStateOf("") }
        var newPrice by remember { mutableStateOf("") }
        var showDateDialog by remember { mutableStateOf(false) }
        var showTimeDialog by remember { mutableStateOf(false) }

        if (showDateDialog) {
            ShireDatePickerDialog(
                onDateSelected = { 
                    newDate = it 
                    showDateDialog = false
                },
                onDismiss = { showDateDialog = false }
            )
        }

        if (showTimeDialog) {
            ShireTimePickerDialog(
                onTimeSelected = {
                    newTime = it
                    showTimeDialog = false
                },
                onDismiss = { showTimeDialog = false }
            )
        }

        AlertDialog(
            onDismissRequest = { showAddActivityDialog = false },
            title = { Text(stringResource(id = R.string.add_activity_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newTitle,
                        onValueChange = { newTitle = it },
                        label = { Text(stringResource(id = R.string.title_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newDesc,
                        onValueChange = { newDesc = it },
                        label = { Text(stringResource(id = R.string.desc_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ShireTextField(
                            value = newDate,
                            onValueChange = { },
                            label = stringResource(id = R.string.date_label),
                            modifier = Modifier.weight(1f),
                            enabled = false,
                            onClick = { showDateDialog = true }
                        )
                        ShireTextField(
                            value = newTime,
                            onValueChange = { },
                            label = stringResource(id = R.string.time_label),
                            modifier = Modifier.weight(1f),
                            enabled = false,
                            onClick = { showTimeDialog = true }
                        )
                    }
                    OutlinedTextField(
                        value = newPrice,
                        onValueChange = { newPrice = it },
                        label = { Text(stringResource(id = R.string.price_label)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val d = try { LocalDate.parse(newDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")) } catch (e: Exception) { null }
                    val t = try { LocalTime.parse(newTime, DateTimeFormatter.ofPattern("HH:mm")) } catch (e: Exception) { null }
                    val p = newPrice.toDoubleOrNull() ?: 0.0

                    viewModel.addPendingActivity(newTitle, newDesc, d, t, p)
                    showAddActivityDialog = false
                }) {
                    Text(stringResource(id = R.string.save_btn))
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddActivityDialog = false }) {
                    Text(stringResource(id = R.string.cancel_btn))
                }
            }
        )
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
                    Text(stringResource(id = R.string.previous_btn), style = MaterialTheme.typography.labelLarge)
                }
            }

            if (currentStep < totalSteps - 1) {
                ShireButton(
                    text = stringResource(id = R.string.next_btn),
                    onClick = onNext,
                    modifier = Modifier.weight(1f),
                    icon = Icons.AutoMirrored.Filled.ArrowForward
                )
            } else {
                ShireButton(
                    text = stringResource(id = R.string.finish_btn),
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
                Text(stringResource(id = R.string.add_another_destination), style = MaterialTheme.typography.labelLarge)
            }
        }

        if (currentStep < totalSteps - 1) {
            TextButton(
                onClick = onSkip,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.skip_step),
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

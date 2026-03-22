package com.example.shire.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shire.ui.components.ShireDatePickerDialog
import com.example.shire.ui.components.ShireTimePickerDialog
import com.example.shire.ui.components.ShireTextField
import com.example.shire.ui.theme.ShireTheme
import com.example.shire.ui.viewmodel.ItineraryViewModel
import com.example.shire.ui.viewmodel.TripsDetailsViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.ZoneId
import androidx.compose.ui.res.stringResource
import com.example.shire.R

data class BudgetDay(val day: Int, val total: Double, val items: List<Pair<String, Double>>)
data class ItineraryItem(
    val time: String,
    val title: String,
    val subtitle: String,
    val cost: String,
    val icon: ImageVector,
    val iconTint: Color,
    val iconBg: Color
)

data class ItineraryDay(
    val dayTitle: String,
    val items: List<ItineraryItem>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailsScreen(
    tripId: String, 
    onNavigateUp: () -> Unit, 
    onNavigate: (String) -> Unit,
    viewModel: TripsDetailsViewModel = hiltViewModel(),
    itineraryViewModel: ItineraryViewModel = hiltViewModel()
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf(stringResource(id = R.string.tab_itinerary), stringResource(id = R.string.tab_gallery), stringResource(id = R.string.tab_budget))

    val tripIdInt = tripId.toIntOrNull() ?: 0
    val trip = viewModel.getTrip(tripIdInt)

    var showAddActivityDialog by remember { mutableStateOf(false) }
    var refreshItineraryTrigger by remember { mutableStateOf(0) }
    
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(itineraryViewModel.errorMessage) {
        itineraryViewModel.errorMessage?.let { error ->
            snackbarHostState.showSnackbar(error)
            itineraryViewModel.errorMessage = null
        }
    }

    val itinerary = remember(trip, refreshItineraryTrigger) {
        if (trip == null) return@remember emptyList<ItineraryDay>()

        // Pre-compute first and last day per hotel to show Check-in / Check-out only once
        val hotelFirstDay = mutableMapOf<Int, Int>()
        val hotelLastDay = mutableMapOf<Int, Int>()
        trip.hotel.forEach { (day, hotelId) ->
            hotelFirstDay[hotelId] = minOf(hotelFirstDay[hotelId] ?: day, day)
            hotelLastDay[hotelId] = maxOf(hotelLastDay[hotelId] ?: day, day)
        }

        // The checkout day is the day AFTER the last hotel night
        val checkoutDay = hotelLastDay.mapValues { (_, lastDay) -> lastDay + 1 }

        val userActivities = itineraryViewModel.getActivitiesForTrip(tripIdInt)
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
        val tripStartDateMs = try { trip.startDate.let { sdf.parse(it)?.time } } catch (e: Exception) { null }

        val activityDays = mutableMapOf<Int, MutableList<com.example.shire.domain.model.Activity>>()
        var maxActivityDay = 0
        if (tripStartDateMs != null) {
            userActivities.forEach { activity ->
                val activityDateMs = java.util.Date.from(activity.date.atStartOfDay(ZoneId.systemDefault()).toInstant()).time
                val diffMs = activityDateMs - tripStartDateMs
                val diffDays = java.util.concurrent.TimeUnit.DAYS.convert(diffMs, java.util.concurrent.TimeUnit.MILLISECONDS).toInt() + 1
                activityDays.getOrPut(diffDays) { mutableListOf() }.add(activity)
                if (diffDays > maxActivityDay) maxActivityDay = diffDays
            }
        }

        val maxDay = listOf(
            trip.hotel.keys.maxOrNull() ?: 0,
            trip.flight.keys.maxOrNull() ?: 0,
            trip.car.keys.maxOrNull() ?: 0,
            trip.places.keys.maxOrNull() ?: 0,
            checkoutDay.values.maxOrNull() ?: 0,
            maxActivityDay
        ).maxOrNull() ?: 0

        val days = mutableListOf<ItineraryDay>()

        for (day in 1..maxDay) {
            val items = mutableListOf<ItineraryItem>()

            trip.flight[day]?.let { id ->
                viewModel.getFlight(id)?.let { flight ->
                    items.add(ItineraryItem("08:00", context.getString(R.string.flight_item_title, flight.flightNumber), "${flight.departureCity} → ${flight.arrivalCity} · Terminal ${flight.terminal}", "${flight.price}€", Icons.Default.Flight, Color(0xFF1976D2), Color(0xFFE3F2FD)))
                }
            }

            // Show Check-in only on the first day of the hotel stay
            trip.hotel[day]?.let { hotelId ->
                if (hotelFirstDay[hotelId] == day) {
                    viewModel.getHotel(hotelId)?.let { hotel ->
                        items.add(ItineraryItem("14:00", context.getString(R.string.checkin_hotel_title, hotel.name), "${hotel.location} · ${hotel.rating}⭐", "${hotel.price}${context.getString(R.string.price_per_night)}", Icons.Default.Hotel, Color(0xFFD84315), Color(0xFFFBE9E7)))
                    }
                }
            }

            // Show Check-out on the day after the last hotel night
            checkoutDay.forEach { (hotelId, coDay) ->
                if (coDay == day) {
                    viewModel.getHotel(hotelId)?.let { hotel ->
                        items.add(ItineraryItem("12:00", context.getString(R.string.checkout_hotel_title, hotel.name), "${hotel.location} · ${hotel.rating}⭐", "", Icons.Default.Hotel, Color(0xFFD84315), Color(0xFFFBE9E7)))
                    }
                }
            }

            trip.places[day]?.forEachIndexed { index, id ->
                viewModel.getPlace(id)?.let { place ->
                    val hour = 16 + index * 2
                    val timeString = "${hour.toString().padStart(2, '0')}:00"
                    items.add(ItineraryItem(timeString, place.name, "${place.location} · ${place.type}", if (place.price == 0.0) context.getString(R.string.free) else "${place.price}€", Icons.Default.Place, Color(0xFFC2185B), Color(0xFFFCE4EC)))
                }
            }

            activityDays[day]?.forEach { activity ->
                val timeString = activity.time.format(DateTimeFormatter.ofPattern("HH:mm"))
                items.add(ItineraryItem(timeString, activity.title, activity.description, if (activity.price == 0.0) context.getString(R.string.free) else "${activity.price}€", Icons.Default.Event, Color(0xFF9C27B0), Color(0xFFF3E5F5)))
            }

            if (items.isNotEmpty()) {
                items.sortBy { it.time }
                days.add(ItineraryDay(context.getString(R.string.day_title_format, day), items))
            }
        }
        days
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (selectedTabIndex == 0) {
                FloatingActionButton(
                    onClick = { showAddActivityDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_activity_title), tint = Color.White)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.trip_details_title), style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val id = tripId.toIntOrNull() ?: 0
                        viewModel.deleteTrip(id)
                        onNavigateUp()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete_trip_desc), tint = MaterialTheme.colorScheme.error)
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
            // Header Image Box
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(24.dp)
                    ) {
                        Text(
                            text = trip?.title ?: stringResource(id = R.string.trip_not_found),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (trip != null) "${trip.startDate} - ${trip.endDate}" else "",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            // Stats Row
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatItem(trip?.hotel?.size?.toString() ?: "0", stringResource(id = R.string.nights_label))
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        StatItem("${trip?.price ?: "0"}€", stringResource(id = R.string.budget_label))
                        VerticalDivider(modifier = Modifier.height(40.dp))
                        StatItem(trip?.places?.size?.toString() ?: "0", stringResource(id = R.string.activities_label))
                    }
                }
            }

            // Custom Tab Row
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    edgePadding = 16.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { 
                                selectedTabIndex = index 
                                if (title == context.getString(R.string.tab_gallery)) {
                                    onNavigate("trip_gallery/$tripId")
                                }
                            },
                            text = {
                                Text(
                                    text = title,
                                    style = if (selectedTabIndex == index) MaterialTheme.typography.labelLarge else MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                                    color = if (selectedTabIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Itinerary Content
            if (selectedTabIndex == 0) {
                itinerary.forEach { day ->
                    item {
                        Text(
                            text = day.dayTitle,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }
                    items(day.items) { item ->
                        ItineraryItemRow(item)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // Budget Content
            if (selectedTabIndex == 2 && trip != null) {
                val userActivities = itineraryViewModel.getActivitiesForTrip(tripIdInt)
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val tripStartDateMs = try { trip.startDate.let { sdf.parse(it)?.time } } catch (e: Exception) { null }

                val activityDays = mutableMapOf<Int, MutableList<com.example.shire.domain.model.Activity>>()
                var maxActivityDay = 0
                if (tripStartDateMs != null) {
                    userActivities.forEach { activity ->
                        val activityDateMs = java.util.Date.from(activity.date.atStartOfDay(ZoneId.systemDefault()).toInstant()).time
                        val diffMs = activityDateMs - tripStartDateMs
                        val diffDays = java.util.concurrent.TimeUnit.DAYS.convert(diffMs, java.util.concurrent.TimeUnit.MILLISECONDS).toInt() + 1
                        activityDays.getOrPut(diffDays) { mutableListOf() }.add(activity)
                        if (diffDays > maxActivityDay) maxActivityDay = diffDays
                    }
                }

                val maxBudgetDay = listOf(
                    trip.hotel.keys.maxOrNull() ?: 0,
                    trip.flight.keys.maxOrNull() ?: 0,
                    trip.car.keys.maxOrNull() ?: 0,
                    trip.places.keys.maxOrNull() ?: 0,
                    maxActivityDay
                ).maxOrNull() ?: 0

                val budgetDays = mutableListOf<BudgetDay>()
                var overallSpent = 0.0

                for (day in 1..maxBudgetDay) {
                    var dayTotal = 0.0
                    val dayItems = mutableListOf<Pair<String, Double>>()

                    trip.flight[day]?.let { id ->
                        viewModel.getFlight(id)?.let { flight ->
                            dayTotal += flight.price
                            dayItems.add(context.getString(R.string.flight_to_format, flight.arrivalCity) to flight.price)
                        }
                    }
                    trip.hotel[day]?.let { id ->
                        viewModel.getHotel(id)?.let { hotel ->
                            dayTotal += hotel.price
                            dayItems.add(hotel.name to hotel.price)
                        }
                    }
                    trip.car[day]?.let { id ->
                        viewModel.getCar(id)?.let { car ->
                            dayTotal += car.pricePerDay
                            dayItems.add(car.model to car.pricePerDay)
                        }
                    }
                    trip.places[day]?.forEach { id ->
                        viewModel.getPlace(id)?.let { place ->
                            dayTotal += place.price
                            dayItems.add(place.name to place.price)
                        }
                    }
                    activityDays[day]?.forEach { activity ->
                        dayTotal += activity.price
                        dayItems.add(activity.title to activity.price)
                    }

                    overallSpent += dayTotal
                    if (dayItems.isNotEmpty()) {
                        budgetDays.add(BudgetDay(day, dayTotal, dayItems))
                    }
                }

                item {
                    BudgetOverviewCard(totalSpent = overallSpent, totalBudget = trip.price)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.daily_breakdown),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(budgetDays) { budgetDay ->
                    Box(modifier = Modifier.padding(horizontal = 24.dp)) {
                        BudgetDayCard(budgetDay)
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
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

                    if (itineraryViewModel.addActivity(tripIdInt, newTitle, newDesc, d, t, p)) {
                        refreshItineraryTrigger++
                        showAddActivityDialog = false
                    }
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

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value, 
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, 
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label, 
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant, 
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun ItineraryItemRow(item: ItineraryItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = item.time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(48.dp)
        )
        
        Surface(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(40.dp),
            shape = CircleShape,
            color = item.iconBg
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = item.icon, 
                    contentDescription = null, 
                    tint = item.iconTint, 
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title, 
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold, 
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = item.subtitle, 
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.cost, 
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold, 
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun BudgetOverviewCard(totalSpent: Double, totalBudget: Double) {
    val percentage = if (totalBudget > 0) (totalSpent / totalBudget).toFloat() else 0f
    val progressColor = if (percentage > 1f) Color.Red else Color(0xFF4CAF50)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(id = R.string.estimated_budget), style = MaterialTheme.typography.labelMedium)
            Text("${totalBudget}€", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(stringResource(id = R.string.spent_format, totalSpent), style = MaterialTheme.typography.bodyMedium)
                if (totalSpent > totalBudget) {
                    Text(stringResource(id = R.string.exceeded), color = Color.Red, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                } else {
                    Text(stringResource(id = R.string.remaining_format, totalBudget - totalSpent), style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { minOf(percentage, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
            )
        }
    }
}

@Composable
fun BudgetDayCard(budgetDay: BudgetDay) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.day_title_format, budgetDay.day), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Text("${budgetDay.total}€", fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            budgetDay.items.forEach { (name, price) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(if (price == 0.0) stringResource(id = R.string.free) else "${price}€", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripDetailsScreenPreview() {
    ShireTheme {
        TripDetailsScreen(tripId = "1", onNavigateUp = {}, onNavigate = {})
    }
}

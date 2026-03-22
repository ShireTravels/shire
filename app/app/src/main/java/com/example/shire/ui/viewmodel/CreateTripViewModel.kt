package com.example.shire.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.*
import com.example.shire.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.LinkedList
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class CreateTripViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val carRepository: CarRepository,
    private val activityRepository: ActivityRepository
) : ViewModel() {

    // ── Repository data ──
    val availableDestinations: List<String>
        get() = (hotelRepository.getHotels().map { it.location } +
                 flightRepository.getFlights().map { it.arrivalCity }).distinct().sorted()

    val hotels: List<Hotel>
        get() = if (tripDestination.isBlank()) emptyList() else hotelRepository.getHotels().filter { it.location.contains(tripDestination, ignoreCase = true) }

    val flights: List<Flight>
        get() = if (tripDestination.isBlank()) emptyList() else flightRepository.getFlights().filter { it.arrivalCity.contains(tripDestination, ignoreCase = true) }

    val cars: List<Car> get() = carRepository.getCars()

    // ── Destination step state ──
    var tripDestination by mutableStateOf("")
    var tripStartDate by mutableStateOf("")
    var tripEndDate by mutableStateOf("")
    var numAdults by mutableIntStateOf(2)
    var numChildren by mutableIntStateOf(0)

    // ── Selection state ──
    var selectedHotel by mutableStateOf<Hotel?>(null)
    var selectedFlight by mutableStateOf<Flight?>(null)
    var selectedCar by mutableStateOf<Car?>(null)

    val pendingActivities = mutableStateListOf<Activity>()

    // ── Accumulated Trip Data ──
    private var accumulatedDays = 0
    private var accumulatedTotalPrice = 0.0
    private val accumulatedHotels = hashMapOf<Int, Int>()
    private val accumulatedFlights = hashMapOf<Int, Int>()
    private val accumulatedCars = hashMapOf<Int, Int>()
    private val destinationsList = mutableListOf<String>()
    private val datesList = mutableListOf<String>()

    var errorMessage by mutableStateOf<String?>(null)

    fun validateDestinationStep(): Boolean {
        Log.i("CreateTripViewModel", "Validating destination step parameters: $tripDestination, $tripStartDate - $tripEndDate")
        if (tripDestination.isBlank()) {
            errorMessage = "El destino no puede estar vacío."
            Log.w("CreateTripViewModel", "Validation failed: Destination empty")
            return false
        }
        if (tripStartDate.isBlank()) {
            errorMessage = "La fecha de inicio no puede estar vacía."
            Log.w("CreateTripViewModel", "Validation failed: Start date empty")
            return false
        }
        if (tripEndDate.isBlank()) {
            errorMessage = "La fecha de fin no puede estar vacía."
            Log.w("CreateTripViewModel", "Validation failed: End date empty")
            return false
        }
        try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val start = sdf.parse(tripStartDate)
            val end = sdf.parse(tripEndDate)
            if (start != null && end != null && start.after(end)) {
                errorMessage = "La fecha de inicio debe ser anterior a la fecha de fin."
                Log.w("CreateTripViewModel", "Validation failed: Start after end")
                return false
            }
        } catch (e: Exception) {
            errorMessage = "Formato de fecha inválido."
            Log.e("CreateTripViewModel", "Validation failed: Date parsing exception", e)
            return false
        }
        errorMessage = null
        return true
    }

    /**
     * Adds the current destination selections to the accumulated trip data
     * and resets the current selection for the next destination.
     */
    fun addCurrentDestination() {
        Log.d("CreateTripViewModel", "Adding current destination to accumulating trip. Current destination: $tripDestination")
        val days = computeTripDays()

        selectedHotel?.let { hotel ->
            for (day in 1..days) { accumulatedHotels[accumulatedDays + day] = hotel.id }
            accumulatedTotalPrice += hotel.price * days
        }

        selectedFlight?.let { flight ->
            accumulatedFlights[accumulatedDays + 1] = flight.id
            accumulatedTotalPrice += flight.price
        }

        selectedCar?.let { car ->
            for (day in 1..days) { accumulatedCars[accumulatedDays + day] = car.id }
            accumulatedTotalPrice += car.pricePerDay * days
        }

        if (tripDestination.isNotBlank()) destinationsList.add(tripDestination)
        if (tripStartDate.isNotBlank() && tripEndDate.isNotBlank()) datesList.add("$tripStartDate - $tripEndDate")

        accumulatedDays += days

        tripDestination = ""
        tripStartDate = ""
        tripEndDate = ""
        selectedHotel = null
        selectedFlight = null
        selectedCar = null
    }

    fun addPendingActivity(title: String, description: String, date: java.time.LocalDate?, time: java.time.LocalTime?, price: Double = 0.0) {
        Log.d("CreateTripViewModel", "addPendingActivity: $title")
        val newActivity = Activity(
            id = (pendingActivities.maxOfOrNull { it.id } ?: 0) + 1,
            tripId = 0,
            title = title,
            description = description,
            date = date ?: java.time.LocalDate.now(),
            time = time ?: java.time.LocalTime.now(),
            price = price
        )
        pendingActivities.add(newActivity)
    }

    fun removePendingActivity(activity: Activity) {
        Log.d("CreateTripViewModel", "removePendingActivity: ${activity.title}")
        pendingActivities.remove(activity)
    }

    fun getMinStartDateMillis(): Long? {
        var minMillis: Long? = null
        try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
            if (datesList.isNotEmpty()) {
                val lastSegment = datesList.last()
                if (lastSegment.contains(" - ")) {
                    val endDateStr = lastSegment.split(" - ")[1]
                    sdf.parse(endDateStr)?.time?.let { minMillis = it }
                }
            } else {
                val today = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone("UTC"))
                today.set(java.util.Calendar.HOUR_OF_DAY, 0)
                today.set(java.util.Calendar.MINUTE, 0)
                today.set(java.util.Calendar.SECOND, 0)
                today.set(java.util.Calendar.MILLISECOND, 0)
                minMillis = today.timeInMillis
            }
        } catch (e: Exception) {}
        return minMillis
    }

    fun getMinEndDateMillis(): Long? {
        try {
            if (tripStartDate.isNotBlank()) {
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                sdf.timeZone = java.util.TimeZone.getTimeZone("UTC")
                sdf.parse(tripStartDate)?.time?.let { return it }
            }
        } catch (e: Exception) {}
        return getMinStartDateMillis()
    }


    /**
     * Computes the number of days from the date strings (dd/MM/yyyy).
     * Falls back to 1 if parsing fails.
     */
    fun computeTripDays(): Int {
        return try {
            val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
            val start = sdf.parse(tripStartDate)
            val end = sdf.parse(tripEndDate)
            if (start != null && end != null) {
                val diff = (end.time - start.time) / (1000 * 60 * 60 * 24)
                maxOf(diff.toInt(), 1)
            } else 1
        } catch (_: Exception) {
            1
        }
    }

    /**
     * Creates the trip from current selections and saves it to the repository.
     * Returns the new trip's ID.
     */
    fun createTrip(): Int {
        if (tripDestination.isNotBlank() || selectedHotel != null || selectedFlight != null || selectedCar != null) {
            addCurrentDestination()
        }

        val newId = (tripRepository.getTrips().maxOfOrNull { it.id } ?: 0) + 1

        val title = if (destinationsList.isNotEmpty()) {
            "Viaje a " + destinationsList.joinToString(", ")
        } else "Nuevo Viaje"

        val finalStartDate = datesList.firstOrNull()?.split(" - ")?.getOrNull(0) ?: tripStartDate
        val finalEndDate = datesList.lastOrNull()?.split(" - ")?.getOrNull(1) ?: tripEndDate

        val tripPrice = accumulatedTotalPrice + pendingActivities.sumOf { it.price }

        val trip = Trip(
            id = newId,
            title = title,
            startDate = finalStartDate,
            endDate = finalEndDate,
            price = tripPrice,
            hotel = accumulatedHotels,
            flight = accumulatedFlights,
            car = accumulatedCars,
            places = hashMapOf(),
            gallery = LinkedList(),
            description = ""
        )

        tripRepository.addTrip(trip)

        pendingActivities.forEach { activity ->
            activityRepository.addActivity(activity.copy(tripId = newId))
        }

        Log.i("CreateTripViewModel", "Trip created successfully with ID: $newId and ${pendingActivities.size} pending activities.")
        return newId
    }
}

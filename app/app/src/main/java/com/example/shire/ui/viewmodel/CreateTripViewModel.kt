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

@HiltViewModel
class CreateTripViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val carRepository: CarRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    // ── Repository data ──
    val availableDestinations: List<String>
        get() = (hotelRepository.getHotels().map { it.location } +
                 flightRepository.getFlights().map { it.arrivalCity } +
                 placeRepository.getPlaces().map { it.location }).distinct().sorted()

    val hotels: List<Hotel>
        get() = if (tripDestination.isBlank()) emptyList() else hotelRepository.getHotels().filter { it.location.contains(tripDestination, ignoreCase = true) }

    val flights: List<Flight>
        get() = if (tripDestination.isBlank()) emptyList() else flightRepository.getFlights().filter { it.arrivalCity.contains(tripDestination, ignoreCase = true) }

    val cars: List<Car> get() = carRepository.getCars()

    val places: List<Place>
        get() = if (tripDestination.isBlank()) emptyList() else placeRepository.getPlaces().filter { it.location.contains(tripDestination, ignoreCase = true) }

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
    val selectedPlaces = mutableStateMapOf<Place, Int>()

    // ── Accumulated Trip Data ──
    private var accumulatedDays = 0
    private var accumulatedTotalPrice = 0.0
    private val accumulatedHotels = hashMapOf<Int, Int>()
    private val accumulatedFlights = hashMapOf<Int, Int>()
    private val accumulatedCars = hashMapOf<Int, Int>()
    private val accumulatedPlaces = hashMapOf<Int, MutableList<Int>>()
    private val destinationsList = mutableListOf<String>()
    private val datesList = mutableListOf<String>()

    /**
     * Adds the current destination selections to the accumulated trip data
     * and resets the current selection for the next destination.
     */
    fun addCurrentDestination() {
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

        selectedPlaces.forEach { (place, localDay) ->
            val targetDay = accumulatedDays + localDay
            accumulatedPlaces.getOrPut(targetDay) { mutableListOf() }.add(place.id)
            accumulatedTotalPrice += place.price
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
        selectedPlaces.clear()
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

    fun togglePlaceSelection(place: Place) {
        if (selectedPlaces.containsKey(place)) {
            selectedPlaces.remove(place)
        } else {
            selectedPlaces[place] = 1
        }
    }

    fun updatePlaceDay(place: Place, day: Int) {
        if (selectedPlaces.containsKey(place)) {
            selectedPlaces[place] = day
        }
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
        if (tripDestination.isNotBlank() || selectedHotel != null || selectedFlight != null || selectedCar != null || selectedPlaces.isNotEmpty()) {
            addCurrentDestination()
        }

        val newId = (tripRepository.getTrips().maxOfOrNull { it.id } ?: 0) + 1

        val title = if (destinationsList.isNotEmpty()) {
            "Viaje a " + destinationsList.joinToString(", ")
        } else "Nuevo Viaje"

        val dates = if (datesList.isNotEmpty()) {
            datesList.joinToString(" y ")
        } else ""

        val trip = Trip(
            id = newId,
            title = title,
            dates = dates,
            price = accumulatedTotalPrice,
            hotel = accumulatedHotels,
            flight = accumulatedFlights,
            car = accumulatedCars,
            places = accumulatedPlaces,
            gallery = LinkedList(),
            notes = ""
        )

        tripRepository.addTrip(trip)
        return newId
    }
}

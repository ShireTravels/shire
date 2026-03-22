package com.example.shire.domain.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.shire.domain.model.Trip
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class TripRepositoryImpl @Inject constructor(
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val carRepository: CarRepository,
    private val placeRepository: PlaceRepository
) : TripRepository {
    // Usamos una lista observable para que Compose detecte cambios.
    private val tripList = mutableStateListOf<Trip>()

    init {
        val hotelParis = hotelRepository.getHotel(101)
        val flightToParis = flightRepository.getFlight(201)
        val carParis = carRepository.getCar(301)
        val placeLouvre = placeRepository.getPlace(401)

        val hotelRome = hotelRepository.getHotel(105)
        val flightToRome = flightRepository.getFlight(205)
        val placeColiseum = placeRepository.getPlace(410)

        // Aquí agregamos datos iniciales de ejemplo
        tripList.addAll(
            listOf(
                Trip(
                    id = 1,
                    title = "Viaje a París",
                    startDate = "12/04/2026",
                    endDate = "20/04/2026",
                    price = 1500.0,
                    hotel = hashMapOf(1 to hotelParis.id, 2 to hotelParis.id, 3 to hotelParis.id),
                    flight = hashMapOf(1 to flightToParis.id),
                    car = hashMapOf(1 to carParis.id, 2 to carParis.id, 3 to carParis.id),
                    places = hashMapOf(1 to mutableListOf(placeLouvre.id)),
                    gallery = java.util.LinkedList(listOf("https://example.com/paris1.jpg", "https://example.com/paris2.jpg")),
                    description = "Llevar ropa de abrigo y reservar entradas al Louvre."
                ),
                Trip(
                    id = 2,
                    title = "Escapada a Roma",
                    startDate = "05/05/2026",
                    endDate = "10/05/2026",
                    price = 850.50,
                    hotel = hashMapOf(1 to hotelRome.id, 2 to hotelRome.id),
                    flight = hashMapOf(1 to flightToRome.id),
                    car = hashMapOf(),
                    places = hashMapOf(1 to mutableListOf(placeColiseum.id), 2 to mutableListOf(placeColiseum.id)),
                    gallery = java.util.LinkedList(listOf("https://example.com/roma1.jpg")),
                    description = "Probar la pizza en Trastevere."
                )
            )
        )
    }

    override fun getTrip(tripId: Int): Trip? {
        Log.d("TripRepo", "Fetching trip with id: $tripId")
        return tripList.find({ it.id == tripId })
    }

    override fun getTrips(): List<Trip> {
        Log.d("TripRepo", "Fetching all trips. Total trips: ${tripList.size}")
        return tripList
    }

    override fun addTrip(trip: Trip): Trip {
        tripList.add(trip)
        Log.i("TripRepo", "Added new trip: ${trip.title} (ID: ${trip.id})")
        return trip
    }

    override fun deleteTrip(tripId: Int): Boolean {
        var tempTrip = tripList.find({it.id == tripId})

        if (tempTrip == null) {
            Log.e("TripRepo", "Failed to delete trip: ID $tripId not found")
            return false
        }

        tripList.remove(tempTrip)
        Log.i("TripRepo", "Deleted trip successfully (ID: $tripId)")
        return true
    }

    override fun updateTrip(trip: Trip): Boolean {
        val index = tripList.indexOfFirst { it.id == trip.id }
        if (index != -1) {
            tripList[index] = trip
            Log.i("TripRepo", "Updated trip successfully (ID: ${trip.id})")
            return true
        }
        Log.e("TripRepo", "Failed to update trip: ID ${trip.id} not found")
        return false
    }

}

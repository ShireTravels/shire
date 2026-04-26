package com.example.shire.domain.repository

import android.content.Context
import android.util.Log
import com.example.shire.db.Activity as DbActivity
import com.example.shire.db.Trip as DbTrip
import com.example.shire.db.User as DbUser
import com.example.shire.db.db
import com.example.shire.domain.model.Trip
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val carRepository: CarRepository,
    private val placeRepository: PlaceRepository
) : TripRepository {

    private val database = db(context)
    private val defaultUserId = 1

    init {
        ensureDefaultUser()
        seedTripsIfNeeded()
        seedActivitiesIfNeeded()
    }

    private fun seedTripsIfNeeded() {
        if (database.getTrips(defaultUserId).isNotEmpty()) return

        val hotelParis = hotelRepository.getHotel(101)
        val flightToParis = flightRepository.getFlight(201)
        val carParis = carRepository.getCar(301)
        val placeLouvre = placeRepository.getPlace(401)

        val hotelRome = hotelRepository.getHotel(105)
        val flightToRome = flightRepository.getFlight(205)
        val placeColiseum = placeRepository.getPlace(410)

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
            .forEach { seedTrip -> database.insertTrip(seedTrip.toDbTrip()) }
    }

    private fun ensureDefaultUser() {
        if (database.getUserById(defaultUserId) != null) return
        database.upsertUser(
            DbUser(
                id = defaultUserId,
                name = "Default User",
                email = "default@shire.local",
                passwordHash = "local",
                createdAt = System.currentTimeMillis()
            )
        )
    }

    private fun seedActivitiesIfNeeded() {
        if (database.getActivitiesByTrip(1).isNotEmpty() || database.getActivitiesByTrip(2).isNotEmpty()) return

        listOf(
            DbActivity(
                id = 1,
                tripId = 1,
                title = "Visita al Louvre",
                description = "Entrada reservada para las 10 AM",
                date = LocalDate.of(2026, 4, 13),
                time = LocalTime.of(10, 0),
                price = 0.0
            ),
            DbActivity(
                id = 2,
                tripId = 1,
                title = "Cena en la Torre Eiffel",
                description = "Cena romantica en el restaurante de la torre",
                date = LocalDate.of(2026, 4, 15),
                time = LocalTime.of(21, 0),
                price = 0.0
            ),
            DbActivity(
                id = 3,
                tripId = 2,
                title = "Tour por el Coliseo",
                description = "Visita guiada al Coliseo y Foro Romano",
                date = LocalDate.of(2026, 5, 6),
                time = LocalTime.of(9, 30),
                price = 0.0
            )
        ).forEach { activity ->
            database.insertActivity(activity)
        }
    }

    override fun getTrip(tripId: Int): Trip? {
        Log.d("TripRepo", "Fetching trip with id: $tripId")
        return database.getTripById(defaultUserId, tripId)?.toDomainTrip()
    }

    override fun getTrips(): List<Trip> {
        val trips = database.getTrips(defaultUserId).map { it.toDomainTrip() }
        Log.d("TripRepo", "Fetching all trips. Total trips: ${trips.size}")
        return trips
    }

    override fun addTrip(trip: Trip): Trip {
        val insertedId = database.insertTrip(trip.toDbTrip()).toInt()
        val persistedTrip = if (trip.id > 0) trip else trip.copy(id = insertedId)
        Log.i("TripRepo", "Added new trip: ${persistedTrip.title} (ID: ${persistedTrip.id})")
        return persistedTrip
    }

    override fun deleteTrip(tripId: Int): Boolean {
        val deleted = database.deleteTrip(defaultUserId, tripId) > 0
        if (!deleted) {
            Log.e("TripRepo", "Failed to delete trip: ID $tripId not found")
            return false
        }
        Log.i("TripRepo", "Deleted trip successfully (ID: $tripId)")
        return true
    }

    override fun updateTrip(trip: Trip): Boolean {
        val exists = database.getTripById(defaultUserId, trip.id) != null
        if (exists) {
            database.insertTrip(trip.toDbTrip())
            Log.i("TripRepo", "Updated trip successfully (ID: ${trip.id})")
            return true
        }
        Log.e("TripRepo", "Failed to update trip: ID ${trip.id} not found")
        return false
    }

    private fun Trip.toDbTrip(): DbTrip = DbTrip(
        id = id,
        userId = defaultUserId,
        title = title,
        startDate = startDate,
        endDate = endDate,
        price = price,
        hotel = hotel,
        flight = flight,
        car = car,
        places = places,
        gallery = gallery,
        description = description
    )

    private fun DbTrip.toDomainTrip(): Trip = Trip(
        id = id,
        title = title,
        startDate = startDate,
        endDate = endDate,
        price = price,
        hotel = hotel,
        flight = flight,
        car = car,
        places = places,
        gallery = gallery,
        description = description
    )

}

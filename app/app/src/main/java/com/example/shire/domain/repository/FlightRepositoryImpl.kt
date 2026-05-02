package com.example.shire.domain.repository

import android.content.Context
import android.util.Log
import com.example.shire.db.Flight as DbFlight
import com.example.shire.db.db
import com.example.shire.domain.model.Flight
import com.example.shire.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FlightRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : FlightRepository {

    private val database = db(context)

    private val seedFlights = run {
        val dateValue = Date()
        listOf(
            Flight(
                id = 201,
                flightNumber = "AF1234",
                company = "Air France",
                departureCity = "Madrid",
                arrivalCity = "Paris",
                departureDate = dateValue,
                arrivalDate = dateValue,
                terminal = 2,
                gate = 14,
                type = "Economy",
                price = 120.0
            ),
            Flight(
                id = 205,
                flightNumber = "AZ5678",
                company = "ITA Airways",
                departureCity = "Madrid",
                arrivalCity = "Rome",
                departureDate = dateValue,
                arrivalDate = dateValue,
                terminal = 4,
                gate = 32,
                type = "Economy",
                price = 90.0
            ),
            Flight(
                id = 210,
                flightNumber = "JL405",
                company = "Japan Airlines",
                departureCity = "Madrid",
                arrivalCity = "Japan",
                departureDate = dateValue,
                arrivalDate = dateValue,
                terminal = 1,
                gate = 55,
                type = "Economy",
                price = 650.0
            ),
            Flight(
                id = 215,
                flightNumber = "AZ012",
                company = "ITA Airways",
                departureCity = "Madrid",
                arrivalCity = "Italy",
                departureDate = dateValue,
                arrivalDate = dateValue,
                terminal = 4,
                gate = 40,
                type = "Economy",
                price = 110.0
            )
        )
    }

    init {
        seedIfNeeded()
    }

    override fun getFlight(flightId: Int): Flight {
        Log.d("FlightRepo", "Fetching flight with id: $flightId")
        return database.getFlightById(flightId)?.toDomainFlight()
            ?: throw NoSuchElementException("Flight with id=$flightId not found")
    }

    override fun getFlights(): List<Flight> {
        val flights = database.getFlights().map { it.toDomainFlight() }
        Log.d("FlightRepo", "Fetching all flights. Count: ${flights.size}")
        return flights
    }

    override fun getUserFlights(user: User): List<Flight> {
        Log.d("FlightRepo", "Fetching flights for user: $user")
        return getFlights()
    }

    override fun addFlight(flight: Flight): Flight {
        val insertedId = database.insertFlight(flight.toDbFlight()).toInt()
        val persistedFlight = if (flight.id > 0) flight else flight.copy(id = insertedId)
        Log.i("FlightRepo", "Added new flight: ${persistedFlight.flightNumber} (ID: ${persistedFlight.id})")
        return persistedFlight
    }

    override fun deleteFlight(flightId: Int): Boolean {
        val removed = database.deleteFlight(flightId) > 0
        if (removed) Log.i("FlightRepo", "Deleted flight successfully (ID: $flightId)")
        else Log.e("FlightRepo", "Failed to delete flight: ID $flightId not found")
        return removed
    }

    override fun updateFlight(flight: Flight): Boolean {
        val exists = database.getFlightById(flight.id) != null
        if (exists) {
            database.insertFlight(flight.toDbFlight())
            Log.i("FlightRepo", "Updated flight successfully (ID: ${flight.id})")
            return true
        }
        Log.e("FlightRepo", "Failed to update flight: ID ${flight.id} not found")
        return false
    }

    private fun seedIfNeeded() {
        if (database.getFlights().isNotEmpty()) return
        seedFlights.forEach { flight -> database.insertFlight(flight.toDbFlight()) }
    }

    private fun Flight.toDbFlight(): DbFlight = DbFlight(
        id = id,
        flightNumber = flightNumber,
        company = company,
        departureCity = departureCity,
        arrivalCity = arrivalCity,
        departureDate = departureDate,
        arrivalDate = arrivalDate,
        terminal = terminal,
        gate = gate,
        type = type,
        price = price
    )

    private fun DbFlight.toDomainFlight(): Flight = Flight(
        id = id,
        flightNumber = flightNumber,
        company = company,
        departureCity = departureCity,
        arrivalCity = arrivalCity,
        departureDate = departureDate,
        arrivalDate = arrivalDate,
        terminal = terminal,
        gate = gate,
        type = type,
        price = price
    )
}

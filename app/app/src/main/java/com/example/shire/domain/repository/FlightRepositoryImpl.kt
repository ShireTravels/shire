package com.example.shire.domain.repository

import com.example.shire.domain.model.Flight
import com.example.shire.domain.model.User
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class FlightRepositoryImpl @Inject constructor() : FlightRepository {

    private val flights = mutableListOf<Flight>()

    init {
        val dateValue = Date()
        flights.add(
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
            )
        )
        flights.add(
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
            )
        )
        flights.add(
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
            )
        )
        flights.add(
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

    override fun getFlight(flightId: Int): Flight {
        Log.d("FlightRepo", "Fetching flight with id: $flightId")
        return flights.first { it.id == flightId }
    }

    override fun getFlights(): List<Flight> {
        Log.d("FlightRepo", "Fetching all flights. Count: ${flights.size}")
        return flights
    }

    override fun getUserFlights(user: User): List<Flight> {
        Log.d("FlightRepo", "Fetching flights for user: $user")
        return flights
    }

    override fun addFlight(flight: Flight): Flight {
        flights.add(flight)
        Log.i("FlightRepo", "Added new flight: ${flight.flightNumber} (ID: ${flight.id})")
        return flight
    }

    override fun deleteFlight(flightId: Int): Boolean {
        val removed = flights.removeAll { it.id == flightId }
        if (removed) Log.i("FlightRepo", "Deleted flight successfully (ID: $flightId)")
        else Log.e("FlightRepo", "Failed to delete flight: ID $flightId not found")
        return removed
    }

    override fun updateFlight(flight: Flight): Boolean {
        val index = flights.indexOfFirst { it.id == flight.id }
        if (index != -1) {
            flights[index] = flight
            Log.i("FlightRepo", "Updated flight successfully (ID: ${flight.id})")
            return true
        }
        Log.e("FlightRepo", "Failed to update flight: ID ${flight.id} not found")
        return false
    }
}

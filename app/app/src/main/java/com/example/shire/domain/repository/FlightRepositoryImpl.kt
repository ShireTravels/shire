package com.example.shire.domain.repository

import com.example.shire.domain.model.Flight
import com.example.shire.domain.model.User
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

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
    }

    override fun getFlight(flightId: Int): Flight {
        return flights.first { it.id == flightId }
    }

    override fun getFlights(): List<Flight> {
        return flights
    }

    override fun getUserFlights(user: User): List<Flight> {
        return flights
    }

    override fun addFlight(flight: Flight): Flight {
        flights.add(flight)
        return flight
    }

    override fun deleteFlight(flightId: Int): Boolean {
        return flights.removeAll { it.id == flightId }
    }

    override fun updateFlight(flight: Flight): Boolean {
        val index = flights.indexOfFirst { it.id == flight.id }
        if (index != -1) {
            flights[index] = flight
            return true
        }
        return false
    }
}

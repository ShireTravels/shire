package com.example.shire.domain.repository

import com.example.shire.domain.model.Flight
import com.example.shire.domain.model.User

interface FlightRepository {
    fun getFlight(flightId: Int): Flight
    fun getFlights(): List<Flight>
    fun getUserFlights(user:  User): List<Flight>
    fun addFlight(flight: Flight): Flight
    fun deleteFlight(flightId: Int): Boolean
    fun updateFlight(flight: Flight): Boolean

    //TODO [lazaropaul]: Create specific functions like update terminal, or gate
}

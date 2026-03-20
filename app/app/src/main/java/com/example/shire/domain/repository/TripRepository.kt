package com.example.shire.domain.repository

import com.example.shire.domain.model.Trip
import com.example.shire.domain.model.User

interface TripRepository {
    fun getTrip(tripId: Int): Trip?
    fun getTrips(): List<Trip>
    fun addTrip(trip: Trip): Trip
    fun deleteTrip(tripId: Int): Boolean
    fun updateTrip(trip: Trip): Boolean
}

package com.example.shire.domain.repository

import com.example.shire.domain.model.Trip
import com.example.shire.domain.model.User
import kotlinx.coroutines.flow.Flow

interface TripRepository {
    fun getTrip(tripId: Int): Flow<Trip?>
    fun getTrips(): Flow<List<Trip>>
    fun addTrip(trip: Trip): Trip
    fun deleteTrip(tripId: Int): Boolean
    fun updateTrip(trip: Trip): Boolean
    fun isTitleDuplicate(title: String): Boolean
}

package com.example.shire.domain.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.shire.domain.model.Trip
import com.example.shire.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TripRepositoryImpl @Inject constructor() : TripRepository {
    // Usamos una lista observable para que Compose detecte cambios.
    private val tripList = mutableStateListOf<Trip>()

    override fun getTrip(tripId: Int): Trip? {
        return tripList.find({ it.id == tripId })
    }

    override fun getTrips(): List<Trip> {
        return tripList
    }

    override fun addTrip(trip: Trip): Trip {
        tripList.add(trip)
        return trip
    }

    override fun deleteTrip(tripId: Int): Boolean {
        var tempTrip = tripList.find({it.id == tripId})

        if (tempTrip == null) {
            return false
        }

        tripList.remove(tempTrip)
        return true
    }

    override fun updateTrip(trip: Trip): Boolean {
        val index = tripList.indexOfFirst { it.id == trip.id }
        if (index != -1) {
            tripList[index] = trip
            return true
        }
        return false
    }

}
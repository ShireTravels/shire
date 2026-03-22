package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.Trip
import com.example.shire.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    val trips: List<Trip>
        get() = repository.getTrips()

    fun deleteTrip(tripId: Int) {
        Log.i("TripsViewModel", "Attempting to delete trip $tripId")
        repository.deleteTrip(tripId)
    }

    fun getTripById(tripId: Int): Trip? {
        Log.d("TripsViewModel", "Getting trip by ID: $tripId")
        return repository.getTrip(tripId)
    }
}

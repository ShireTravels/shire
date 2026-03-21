package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.Trip
import com.example.shire.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    val trips: List<Trip>
        get() = repository.getTrips()

    fun deleteTrip(tripId: Int) {
        repository.deleteTrip(tripId)
    }

    fun getTripById(tripId: Int): Trip? {
        return repository.getTrip(tripId)
    }
}

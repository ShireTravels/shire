package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shire.domain.model.Trip
import com.example.shire.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val repository: TripRepository
) : ViewModel() {

    private val _trips = MutableStateFlow<List<Trip>>(emptyList())
    val trips: StateFlow<List<Trip>> = _trips.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTrips().collect {
                _trips.value = it
            }
        }
    }

    fun deleteTrip(tripId: Int) {
        Log.i("TripsViewModel", "Attempting to delete trip $tripId")
        viewModelScope.launch {
            repository.deleteTrip(tripId)
        }
    }

    fun getTripById(tripId: Int): Trip? {
        Log.d("TripsViewModel", "Getting trip by ID: $tripId")
        return repository.getTrip(tripId)
    }
}

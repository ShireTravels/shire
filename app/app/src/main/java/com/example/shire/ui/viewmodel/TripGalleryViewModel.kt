package com.example.shire.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.Trip
import com.example.shire.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TripGalleryViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tripId: Int = savedStateHandle.get<String>("tripId")?.toIntOrNull() ?: -1

    private val _trip = MutableStateFlow<Trip?>(null)
    val trip: StateFlow<Trip?> = _trip.asStateFlow()

    init {
        loadTrip()
    }

    private fun loadTrip() {
        if (tripId != -1) {
            _trip.value = tripRepository.getTrip(tripId)
        }
    }

    fun addPhoto(url: String) {
        _trip.value?.let { currentTrip ->
            currentTrip.gallery.add(url)
            tripRepository.updateTrip(currentTrip)
            loadTrip() // Refresh state
        }
    }

    fun removePhoto(url: String) {
        _trip.value?.let { currentTrip ->
            currentTrip.gallery.remove(url)
            tripRepository.updateTrip(currentTrip)
            loadTrip() // Refresh state
        }
    }
}

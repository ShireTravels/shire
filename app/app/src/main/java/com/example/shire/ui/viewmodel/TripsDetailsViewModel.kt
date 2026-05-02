package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.*
import com.example.shire.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TripsDetailsViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val carRepository: CarRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val _trip = MutableStateFlow<Trip?>(null)
    val trip: StateFlow<Trip?> = _trip.asStateFlow()

    fun loadTrip(tripId: Int) {
        viewModelScope.launch {
            tripRepository.getTrip(tripId).collect {
                _trip.value = it
            }
        }
    }

    fun getTrip(tripId: Int): Trip? {
        return _trip.value
    }
    
    fun getHotel(id: Int): Hotel? = runCatching { hotelRepository.getHotel(id) }.getOrNull().also { Log.d("TripsDetailsVM", "Fetching hotel $id") }
    fun getFlight(id: Int): Flight? = runCatching { flightRepository.getFlight(id) }.getOrNull().also { Log.d("TripsDetailsVM", "Fetching flight $id") }
    fun getCar(id: Int): Car? = runCatching { carRepository.getCar(id) }.getOrNull().also { Log.d("TripsDetailsVM", "Fetching car $id") }
    fun getPlace(id: Int): Place? = runCatching { placeRepository.getPlace(id) }.getOrNull().also { Log.d("TripsDetailsVM", "Fetching place $id") }

    fun deleteTrip(tripId: Int): Boolean {
        Log.i("TripsDetailsVM", "Attempting to delete trip $tripId")
        return tripRepository.deleteTrip(tripId)
    }
}

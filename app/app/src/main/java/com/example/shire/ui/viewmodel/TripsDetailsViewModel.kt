package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.*
import com.example.shire.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class TripsDetailsViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val carRepository: CarRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    fun getTrip(tripId: Int): Trip? {
        Log.d("TripsDetailsVM", "Fetching trip details for $tripId")
        return tripRepository.getTrip(tripId)
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

package com.example.shire.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.*
import com.example.shire.domain.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TripsDetailsViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val hotelRepository: HotelRepository,
    private val flightRepository: FlightRepository,
    private val carRepository: CarRepository,
    private val placeRepository: PlaceRepository
) : ViewModel() {

    fun getTrip(tripId: Int): Trip? = tripRepository.getTrip(tripId)
    
    fun getHotel(id: Int): Hotel? = runCatching { hotelRepository.getHotel(id) }.getOrNull()
    fun getFlight(id: Int): Flight? = runCatching { flightRepository.getFlight(id) }.getOrNull()
    fun getCar(id: Int): Car? = runCatching { carRepository.getCar(id) }.getOrNull()
    fun getPlace(id: Int): Place? = runCatching { placeRepository.getPlace(id) }.getOrNull()
}

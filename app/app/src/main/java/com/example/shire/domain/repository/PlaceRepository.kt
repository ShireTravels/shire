package com.example.shire.domain.repository

import com.example.shire.domain.model.Place

interface PlaceRepository {
    fun getPlace(placeId: Int): Place
    fun getPlaces(): List<Place>
    fun addPlace(place: Place): Place
    fun deletePlace(placeId: Int): Boolean
    fun updatePlace(place: Place): Boolean
}

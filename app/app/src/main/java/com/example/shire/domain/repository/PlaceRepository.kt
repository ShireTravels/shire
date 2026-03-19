package com.example.shire.domain.repository

interface PlaceRepository {
    fun getPlace(placeId: Int): PlaceRepository
    fun getPlaces(): List<PlaceRepository>
    fun addPlace(place: PlaceRepository): PlaceRepository
    fun deletePlace(placeId: Int): Boolean
    fun updatePlace(place: PlaceRepository): Boolean
}
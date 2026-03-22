package com.example.shire.domain.repository

import com.example.shire.domain.model.Place
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class PlaceRepositoryImpl @Inject constructor() : PlaceRepository {

    private val places = mutableListOf<Place>()

    init {
        val dateValue = Date()
        places.add(
            Place(
                id = 401,
                name = "Louvre Museum",
                location = "Paris",
                type = "Museum",
                rating = 4.9f,
                imageUrl = "https://example.com/louvre.jpg",
                openHour = dateValue,
                closeHour = dateValue,
                price = 17.0
            )
        )
        places.add(
            Place(
                id = 410,
                name = "Colosseum",
                location = "Rome",
                type = "Monument",
                rating = 4.8f,
                imageUrl = "https://example.com/colosseum.jpg",
                openHour = dateValue,
                closeHour = dateValue,
                price = 20.0
            )
        )
        places.add(
            Place(
                id = 420,
                name = "Mount Fuji",
                location = "Japan",
                type = "Nature",
                rating = 5.0f,
                imageUrl = "https://example.com/fuji.jpg",
                openHour = dateValue,
                closeHour = dateValue,
                price = 0.0
            )
        )
        places.add(
            Place(
                id = 430,
                name = "Uffizi Gallery",
                location = "Italy",
                type = "Museum",
                rating = 4.9f,
                imageUrl = "https://example.com/uffizi.jpg",
                openHour = dateValue,
                closeHour = dateValue,
                price = 25.0
            )
        )
    }

    override fun getPlace(placeId: Int): Place {
        Log.d("PlaceRepo", "Fetching place with id: $placeId")
        return places.first { it.id == placeId }
    }

    override fun getPlaces(): List<Place> {
        Log.d("PlaceRepo", "Fetching all places. Count: ${places.size}")
        return places
    }

    override fun addPlace(place: Place): Place {
        places.add(place)
        Log.i("PlaceRepo", "Added new place: ${place.name} (ID: ${place.id})")
        return place
    }

    override fun deletePlace(placeId: Int): Boolean {
        val removed = places.removeAll { it.id == placeId }
        if (removed) Log.i("PlaceRepo", "Deleted place successfully (ID: $placeId)")
        else Log.e("PlaceRepo", "Failed to delete place: ID $placeId not found")
        return removed
    }

    override fun updatePlace(place: Place): Boolean {
        val index = places.indexOfFirst { it.id == place.id }
        if (index != -1) {
            places[index] = place
            Log.i("PlaceRepo", "Updated place successfully (ID: ${place.id})")
            return true
        }
        Log.e("PlaceRepo", "Failed to update place: ID ${place.id} not found")
        return false
    }
}

package com.example.shire.domain.repository

import android.content.Context
import android.util.Log
import com.example.shire.db.Place as DbPlace
import com.example.shire.db.db
import com.example.shire.domain.model.Place
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlaceRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : PlaceRepository {

    private val database = db(context)

    private val seedPlaces = run {
        val dateValue = Date()
        listOf(
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
            ),
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
            ),
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
            ),
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

    init {
        seedIfNeeded()
    }

    override fun getPlace(placeId: Int): Place {
        Log.d("PlaceRepo", "Fetching place with id: $placeId")
        return database.getPlaceById(placeId)?.toDomainPlace()
            ?: throw NoSuchElementException("Place with id=$placeId not found")
    }

    override fun getPlaces(): List<Place> {
        val places = database.getPlaces().map { it.toDomainPlace() }
        Log.d("PlaceRepo", "Fetching all places. Count: ${places.size}")
        return places
    }

    override fun addPlace(place: Place): Place {
        val insertedId = database.insertPlace(place.toDbPlace()).toInt()
        val persistedPlace = if (place.id > 0) place else place.copy(id = insertedId)
        Log.i("PlaceRepo", "Added new place: ${persistedPlace.name} (ID: ${persistedPlace.id})")
        return persistedPlace
    }

    override fun deletePlace(placeId: Int): Boolean {
        val removed = database.deletePlace(placeId) > 0
        if (removed) Log.i("PlaceRepo", "Deleted place successfully (ID: $placeId)")
        else Log.e("PlaceRepo", "Failed to delete place: ID $placeId not found")
        return removed
    }

    override fun updatePlace(place: Place): Boolean {
        val exists = database.getPlaceById(place.id) != null
        if (exists) {
            database.insertPlace(place.toDbPlace())
            Log.i("PlaceRepo", "Updated place successfully (ID: ${place.id})")
            return true
        }
        Log.e("PlaceRepo", "Failed to update place: ID ${place.id} not found")
        return false
    }

    private fun seedIfNeeded() {
        if (database.getPlaces().isNotEmpty()) return
        seedPlaces.forEach { place -> database.insertPlace(place.toDbPlace()) }
    }

    private fun Place.toDbPlace(): DbPlace = DbPlace(
        id = id,
        name = name,
        location = location,
        type = type,
        rating = rating,
        imageUrl = imageUrl,
        openHour = openHour,
        closeHour = closeHour,
        price = price
    )

    private fun DbPlace.toDomainPlace(): Place = Place(
        id = id,
        name = name,
        location = location,
        type = type,
        rating = rating,
        imageUrl = imageUrl,
        openHour = openHour,
        closeHour = closeHour,
        price = price
    )
}

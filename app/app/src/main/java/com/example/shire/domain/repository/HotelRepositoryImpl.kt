package com.example.shire.domain.repository

import android.content.Context
import android.util.Log
import com.example.shire.db.Hotel as DbHotel
import com.example.shire.db.db
import com.example.shire.domain.model.Hotel
import com.example.shire.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : HotelRepository {

    private val database = db(context)

    private val seedHotels = listOf(
        Hotel(
            id = 101,
            name = "Le Meurice",
            location = "Paris",
            rating = 4.8f,
            imageUrl = "https://example.com/lemeurice.jpg",
            amenities = listOf("WiFi", "Pool", "Spa"),
            description = "Luxury hotel in the center of Paris.",
            price = 300.0
        ),
        Hotel(
            id = 105,
            name = "Hotel Artemide",
            location = "Rome",
            rating = 4.7f,
            imageUrl = "https://example.com/artemide.jpg",
            amenities = listOf("WiFi", "Breakfast"),
            description = "Comfortable stay near Termini.",
            price = 150.0
        ),
        Hotel(
            id = 110,
            name = "Tokyo Grand",
            location = "Japan",
            rating = 4.9f,
            imageUrl = "https://example.com/tokyogrand.jpg",
            amenities = listOf("WiFi", "Onsen", "Breakfast"),
            description = "Authentic Japanese experience in the heart of Tokyo.",
            price = 200.0
        ),
        Hotel(
            id = 115,
            name = "Milan Central Hotel",
            location = "Italy",
            rating = 4.6f,
            imageUrl = "https://example.com/milancentral.jpg",
            amenities = listOf("WiFi", "Gym"),
            description = "Modern hotel near the fashion district.",
            price = 180.0
        )
    )

    init {
        seedIfNeeded()
    }

    override fun getHotel(hotelId: Int): Hotel {
        Log.d("HotelRepo", "Fetching hotel with id: $hotelId")
        return database.getHotelById(hotelId)?.toDomainHotel()
            ?: throw NoSuchElementException("Hotel with id=$hotelId not found")
    }

    override fun getHotels(): List<Hotel> {
        val hotels = database.getHotels().map { it.toDomainHotel() }
        Log.d("HotelRepo", "Fetching all hotels. Count: ${hotels.size}")
        return hotels
    }

    override fun getUserHotels(user: User): List<Hotel> {
        Log.d("HotelRepo", "Fetching hotels for user: $user")
        return getHotels()
    }

    override fun addHotel(hotel: Hotel): Hotel {
        val insertedId = database.insertHotel(hotel.toDbHotel()).toInt()
        val persistedHotel = if (hotel.id > 0) hotel else hotel.copy(id = insertedId)
        Log.i("HotelRepo", "Added new hotel: ${persistedHotel.name} (ID: ${persistedHotel.id})")
        return persistedHotel
    }

    override fun deleteHotel(hotelId: Int): Boolean {
        val removed = database.deleteHotel(hotelId) > 0
        if (removed) Log.i("HotelRepo", "Deleted hotel successfully (ID: $hotelId)")
        else Log.e("HotelRepo", "Failed to delete hotel: ID $hotelId not found")
        return removed
    }

    override fun updateHotel(hotel: Hotel): Boolean {
        val exists = database.getHotelById(hotel.id) != null
        if (exists) {
            database.insertHotel(hotel.toDbHotel())
            Log.i("HotelRepo", "Updated hotel successfully (ID: ${hotel.id})")
            return true
        }
        Log.e("HotelRepo", "Failed to update hotel: ID ${hotel.id} not found")
        return false
    }

    private fun seedIfNeeded() {
        if (database.getHotels().isNotEmpty()) return
        seedHotels.forEach { hotel -> database.insertHotel(hotel.toDbHotel()) }
    }

    private fun Hotel.toDbHotel(): DbHotel = DbHotel(
        id = id,
        name = name,
        location = location,
        rating = rating,
        imageUrl = imageUrl,
        amenities = amenities,
        description = description,
        price = price
    )

    private fun DbHotel.toDomainHotel(): Hotel = Hotel(
        id = id,
        name = name,
        location = location,
        rating = rating,
        imageUrl = imageUrl,
        amenities = amenities,
        description = description,
        price = price
    )
}

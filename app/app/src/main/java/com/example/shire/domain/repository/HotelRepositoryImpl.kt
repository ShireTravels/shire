package com.example.shire.domain.repository

import com.example.shire.domain.model.Hotel
import com.example.shire.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class HotelRepositoryImpl @Inject constructor() : HotelRepository {

    private val hotels = mutableListOf<Hotel>()

    init {
        hotels.add(
            Hotel(
                id = 101,
                name = "Le Meurice",
                location = "Paris",
                rating = 4.8f,
                imageUrl = "https://example.com/lemeurice.jpg",
                amenities = listOf("WiFi", "Pool", "Spa"),
                description = "Luxury hotel in the center of Paris.",
                price = 300.0
            )
        )
        hotels.add(
            Hotel(
                id = 105,
                name = "Hotel Artemide",
                location = "Rome",
                rating = 4.7f,
                imageUrl = "https://example.com/artemide.jpg",
                amenities = listOf("WiFi", "Breakfast"),
                description = "Comfortable stay near Termini.",
                price = 150.0
            )
        )
        hotels.add(
            Hotel(
                id = 110,
                name = "Tokyo Grand",
                location = "Japan",
                rating = 4.9f,
                imageUrl = "https://example.com/tokyogrand.jpg",
                amenities = listOf("WiFi", "Onsen", "Breakfast"),
                description = "Authentic Japanese experience in the heart of Tokyo.",
                price = 200.0
            )
        )
        hotels.add(
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
    }

    override fun getHotel(hotelId: Int): Hotel {
        Log.d("HotelRepo", "Fetching hotel with id: $hotelId")
        return hotels.first { it.id == hotelId }
    }

    override fun getHotels(): List<Hotel> {
        Log.d("HotelRepo", "Fetching all hotels. Count: ${hotels.size}")
        return hotels
    }

    override fun getUserHotels(user: User): List<Hotel> {
        Log.d("HotelRepo", "Fetching hotels for user: $user")
        return hotels
    }

    override fun addHotel(hotel: Hotel): Hotel {
        hotels.add(hotel)
        Log.i("HotelRepo", "Added new hotel: ${hotel.name} (ID: ${hotel.id})")
        return hotel
    }

    override fun deleteHotel(hotelId: Int): Boolean {
        val removed = hotels.removeAll { it.id == hotelId }
        if (removed) Log.i("HotelRepo", "Deleted hotel successfully (ID: $hotelId)")
        else Log.e("HotelRepo", "Failed to delete hotel: ID $hotelId not found")
        return removed
    }

    override fun updateHotel(hotel: Hotel): Boolean {
        val index = hotels.indexOfFirst { it.id == hotel.id }
        if (index != -1) {
            hotels[index] = hotel
            Log.i("HotelRepo", "Updated hotel successfully (ID: ${hotel.id})")
            return true
        }
        Log.e("HotelRepo", "Failed to update hotel: ID ${hotel.id} not found")
        return false
    }
}

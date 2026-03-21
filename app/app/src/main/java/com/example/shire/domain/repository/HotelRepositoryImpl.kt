package com.example.shire.domain.repository

import com.example.shire.domain.model.Hotel
import com.example.shire.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

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
    }

    override fun getHotel(hotelId: Int): Hotel {
        return hotels.first { it.id == hotelId }
    }

    override fun getHotels(): List<Hotel> {
        return hotels
    }

    override fun getUserHotels(user: User): List<Hotel> {
        return hotels
    }

    override fun addHotel(hotel: Hotel): Hotel {
        hotels.add(hotel)
        return hotel
    }

    override fun deleteHotel(hotelId: Int): Boolean {
        return hotels.removeAll { it.id == hotelId }
    }

    override fun updateHotel(hotel: Hotel): Boolean {
        val index = hotels.indexOfFirst { it.id == hotel.id }
        if (index != -1) {
            hotels[index] = hotel
            return true
        }
        return false
    }
}

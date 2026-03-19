package com.example.shire.domain.repository

import com.example.shire.domain.model.Hotel
import com.example.shire.domain.model.User

interface HotelRepository {
    fun getHotel(hotelId: Int): Hotel
    fun getHotels(): List<Hotel>
    fun getUserHotels(user: User): List<Hotel>
    fun addHotel(hotel: Hotel): Hotel
    fun deleteHotel(hotelId: Int): Boolean
    fun updateHotel(hotel: Hotel): Boolean
}
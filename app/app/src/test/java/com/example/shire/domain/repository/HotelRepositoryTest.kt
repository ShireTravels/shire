package com.example.shire.domain.repository

import com.example.shire.domain.model.Hotel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HotelRepositoryTest {
    private lateinit var hotelRepository: HotelRepositoryImpl

    @Before
    fun setUp() {
        hotelRepository = HotelRepositoryImpl()
    }

    @Test
    fun testGetHotels_returnsInitialList() {
        val hotels = hotelRepository.getHotels()
        assertTrue(hotels.isNotEmpty())
        assertTrue(hotels.any { it.id == 101 })
    }

    @Test
    fun testGetHotel_existingId_returnsHotel() {
        val hotel = hotelRepository.getHotel(101)
        assertNotNull(hotel)
        assertEquals("Le Meurice", hotel.name)
    }

    @Test
    fun testAddHotel_addsToList() {
        val initialSize = hotelRepository.getHotels().size
        val newHotel = Hotel(id = 999, name = "Test Hotel", location = "Test", rating = 5.0f, imageUrl = "", amenities = emptyList(), description = "", price = 100.0)
        
        val added = hotelRepository.addHotel(newHotel)
        assertEquals(newHotel, added)
        assertEquals(initialSize + 1, hotelRepository.getHotels().size)
    }

    @Test
    fun testDeleteHotel_removesFromList() {
        val initialSize = hotelRepository.getHotels().size
        val success = hotelRepository.deleteHotel(101)
        
        assertTrue(success)
        assertEquals(initialSize - 1, hotelRepository.getHotels().size)
    }

    @Test
    fun testUpdateHotel_modifiesExistingHotel() {
        val existingHotel = hotelRepository.getHotel(101)
        val updatedHotel = existingHotel.copy(name = "Updated Name")
        
        val success = hotelRepository.updateHotel(updatedHotel)
        assertTrue(success)
        assertEquals("Updated Name", hotelRepository.getHotel(101).name)
    }
}

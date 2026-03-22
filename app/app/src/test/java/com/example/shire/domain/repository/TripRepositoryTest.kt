package com.example.shire.domain.repository

import com.example.shire.domain.model.Trip
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class TripRepositoryTest {

    private lateinit var repository: TripRepositoryImpl

    @Before
    fun setUp() {
        repository = TripRepositoryImpl(
            HotelRepositoryImpl(),
            FlightRepositoryImpl(),
            CarRepositoryImpl(),
            PlaceRepositoryImpl()
        )
    }

    @Test
    fun addTrip_increasesCountAndReturnsValidId() {
        val initialSize = repository.getTrips().size
        val newTrip = Trip(
            id = 10,
            title = "Test Trip",
            startDate = "10/10/2026",
            endDate = "20/10/2026",
            price = 500.0,
            hotel = hashMapOf(),
            flight = hashMapOf(),
            car = hashMapOf(),
            places = hashMapOf(),
            gallery = java.util.LinkedList(),
            description = "Test Desc"
        )
        val addedTrip = repository.addTrip(newTrip)
        
        assertEquals(10, addedTrip.id)
        assertEquals(initialSize + 1, repository.getTrips().size)
    }

    @Test
    fun getTrip_returnsCorrectTrip() {
        val newTrip = Trip(0, "Test Trip", "10/10/2026", "20/10/2026", 500.0, hashMapOf(), hashMapOf(), hashMapOf(), hashMapOf(), java.util.LinkedList(), "Desc")
        val addedTrip = repository.addTrip(newTrip)
        val retrieved = repository.getTrip(addedTrip.id)
        
        assertNotNull(retrieved)
        assertEquals("Test Trip", retrieved?.title)
    }

    @Test
    fun updateTrip_modifiesExistingData() {
        val newTrip = Trip(0, "Old Title", "10/10/2026", "20/10/2026", 500.0, hashMapOf(), hashMapOf(), hashMapOf(), hashMapOf(), java.util.LinkedList(), "Desc")
        val addedTrip = repository.addTrip(newTrip)
        
        val retrieved = repository.getTrip(addedTrip.id)!!
        val updatedTrip = retrieved.copy(title = "New Title")
        val success = repository.updateTrip(updatedTrip)
        
        assertTrue(success)
        assertEquals("New Title", repository.getTrip(addedTrip.id)?.title)
    }

    @Test
    fun deleteTrip_removesTrip() {
        val newTrip = Trip(0, "To Delete", "10/10/2026", "20/10/2026", 500.0, hashMapOf(), hashMapOf(), hashMapOf(), hashMapOf(), java.util.LinkedList(), "Desc")
        val addedTrip = repository.addTrip(newTrip)
        val success = repository.deleteTrip(addedTrip.id)
        
        assertTrue(success)
        assertNull(repository.getTrip(addedTrip.id))
    }
}

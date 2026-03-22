package com.example.shire.domain.repository

import com.example.shire.domain.model.Trip
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.LinkedList

class TripRepositoryTest {
    private lateinit var tripRepository: TripRepositoryImpl

    @Before
    fun setUp() {
        val hotelRepository = HotelRepositoryImpl()
        val flightRepository = FlightRepositoryImpl()
        val carRepository = CarRepositoryImpl()
        val placeRepository = PlaceRepositoryImpl()
        
        tripRepository = TripRepositoryImpl(
            hotelRepository,
            flightRepository,
            carRepository,
            placeRepository
        )
    }

    @Test
    fun testGetTrips_returnsInitialList() {
        val trips = tripRepository.getTrips()
        assertTrue(trips.isNotEmpty())
    }

    @Test
    fun testGetTrip_existingId_returnsTrip() {
        val trip = tripRepository.getTrip(1)
        assertNotNull(trip)
        assertEquals("Viaje a París", trip?.title)
    }

    @Test
    fun testAddTrip_addsToList() {
        val initialSize = tripRepository.getTrips().size
        val newTrip = Trip(
            id = 999,
            title = "Test Trip",
            dates = "01/01/2026 - 05/01/2026",
            price = 500.0,
            hotel = hashMapOf(),
            flight = hashMapOf(),
            car = hashMapOf(),
            places = hashMapOf(),
            gallery = LinkedList(),
            notes = "Test notes"
        )
        
        tripRepository.addTrip(newTrip)
        assertEquals(initialSize + 1, tripRepository.getTrips().size)
    }

    @Test
    fun testDeleteTrip_removesFromList() {
        val initialSize = tripRepository.getTrips().size
        val success = tripRepository.deleteTrip(1)
        
        assertTrue(success)
        assertEquals(initialSize - 1, tripRepository.getTrips().size)
    }

    @Test
    fun testUpdateTrip_modifiesExistingTrip() {
        val existing = tripRepository.getTrip(1)!!
        val updated = existing.copy(title = "Updated Paris")
        
        val success = tripRepository.updateTrip(updated)
        assertTrue(success)
        assertEquals("Updated Paris", tripRepository.getTrip(1)?.title)
    }
}

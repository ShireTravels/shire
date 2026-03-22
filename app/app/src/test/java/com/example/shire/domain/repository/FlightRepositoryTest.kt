package com.example.shire.domain.repository

import com.example.shire.domain.model.Flight
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Date

class FlightRepositoryTest {
    private lateinit var flightRepository: FlightRepositoryImpl

    @Before
    fun setUp() {
        flightRepository = FlightRepositoryImpl()
    }

    @Test
    fun testGetFlights_returnsInitialList() {
        val flights = flightRepository.getFlights()
        assertTrue(flights.isNotEmpty())
    }

    @Test
    fun testGetFlight_existingId_returnsFlight() {
        val flight = flightRepository.getFlight(201)
        assertNotNull(flight)
        assertEquals("AF1234", flight.flightNumber)
    }

    @Test
    fun testAddFlight_addsToList() {
        val initialSize = flightRepository.getFlights().size
        val newFlight = Flight(id = 999, flightNumber = "TEST1", company = "TestAir", departureCity = "A", arrivalCity = "B", departureDate = Date(), arrivalDate = Date(), terminal = 1, gate = 1, type = "Direct", price = 100.0)
        
        flightRepository.addFlight(newFlight)
        assertEquals(initialSize + 1, flightRepository.getFlights().size)
    }

    @Test
    fun testDeleteFlight_removesFromList() {
        val initialSize = flightRepository.getFlights().size
        val success = flightRepository.deleteFlight(201)
        
        assertTrue(success)
        assertEquals(initialSize - 1, flightRepository.getFlights().size)
    }

    @Test
    fun testUpdateFlight_modifiesExistingFlight() {
        val existing = flightRepository.getFlight(201)
        val updated = existing.copy(company = "UpdatedAir")
        
        val success = flightRepository.updateFlight(updated)
        assertTrue(success)
        assertEquals("UpdatedAir", flightRepository.getFlight(201).company)
    }
}

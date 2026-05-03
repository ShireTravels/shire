package com.example.shire.domain.repository

import android.content.Context
import com.example.shire.db.dbImpl
import com.example.shire.db.Flight as DbFlight
import com.example.shire.domain.model.Flight
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class FlightRepositoryTest {

    private lateinit var repository: FlightRepositoryImpl
    private lateinit var mockDb: dbImpl
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockkStatic("com.example.shire.db.DbKt")
        mockDb = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)

        every { com.example.shire.db.db(any()) } returns mockDb

        every { mockDb.getFlightById(301) } returns DbFlight(
            id = 301,
            flightNumber = "AF123",
            company = "Air France",
            departureCity = "Madrid",
            arrivalCity = "Paris",
            departureDate = Date(),
            arrivalDate = Date(),
            terminal = 1,
            gate = 14,
            type = "Direct",
            price = 120.0
        )

        every { mockDb.getFlights() } returns emptyList()
        every { mockDb.insertFlight(any()) } returns 1L

        repository = FlightRepositoryImpl(mockContext)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getFlight returns mapped domain object`() {
        val flight = repository.getFlight(301)
        assertEquals("AF123", flight.flightNumber)
    }
}

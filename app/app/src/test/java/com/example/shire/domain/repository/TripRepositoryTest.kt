package com.example.shire.domain.repository

import android.content.Context
import com.example.shire.db.dbImpl
import com.example.shire.db.Trip as DbTrip
import com.example.shire.domain.model.Trip
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.LinkedList
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

class TripRepositoryTest {

    private lateinit var repository: TripRepositoryImpl
    private lateinit var mockDb: dbImpl
    private lateinit var mockAuthRepo: AuthRepository
    private lateinit var mockHotelRepo: HotelRepository
    private lateinit var mockFlightRepo: FlightRepository
    private lateinit var mockCarRepo: CarRepository
    private lateinit var mockPlaceRepo: PlaceRepository
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockkStatic("com.example.shire.db.DbKt")
        mockDb = mockk(relaxed = true)
        mockAuthRepo = mockk(relaxed = true)
        mockHotelRepo = mockk(relaxed = true)
        mockFlightRepo = mockk(relaxed = true)
        mockCarRepo = mockk(relaxed = true)
        mockPlaceRepo = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)

        every { com.example.shire.db.db(any()) } returns mockDb
        every { mockAuthRepo.getLoggedInUser() } returns com.example.shire.domain.model.LoggedInUser(1, "Test", "test@test.com")

        every { mockDb.getTripById(1, 601) } returns flowOf(DbTrip(
            id = 601,
            userId = 1,
            title = "Mock Trip",
            startDate = "01/01/2026",
            endDate = "10/01/2026",
            price = 500.0,
            hotel = hashMapOf(),
            flight = hashMapOf(),
            car = hashMapOf(),
            places = hashMapOf(),
            gallery = LinkedList(),
            description = "Mock Description"
        ))

        every { mockDb.getTripsSync(1) } returns emptyList()
        every { mockDb.insertTrip(any()) } returns 1L

        repository = TripRepositoryImpl(
            mockContext,
            mockHotelRepo,
            mockFlightRepo,
            mockCarRepo,
            mockPlaceRepo,
            mockAuthRepo
        )
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getTrip returns mapped domain object flow`() = runBlocking {
        val tripFlow = repository.getTrip(601)
        val trip = tripFlow.first()
        assertEquals("Mock Trip", trip?.title)
    }
}

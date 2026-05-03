package com.example.shire.domain.repository

import com.example.shire.db.dbImpl
import com.example.shire.db.Trip as DbTrip
import com.example.shire.domain.model.LoggedInUser
import com.example.shire.domain.model.Trip
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.LinkedList

class TripRepositoryTest {

    private lateinit var repository: TripRepositoryImpl
    private val database: dbImpl = mockk(relaxed = true)
    private val authRepository: AuthRepository = mockk()
    private val hotelRepository: HotelRepository = mockk()
    private val flightRepository: FlightRepository = mockk()
    private val carRepository: CarRepository = mockk()
    private val placeRepository: PlaceRepository = mockk()

    private val testUser = LoggedInUser(1, "Test User", "test@example.com")

    @Before
    fun setUp() {
        coEvery { authRepository.getLoggedInUser() } returns testUser
        coEvery { authRepository.loggedInUserFlow } returns flowOf(testUser)
        
        repository = TripRepositoryImpl(
            database,
            authRepository,
            hotelRepository,
            flightRepository,
            carRepository,
            placeRepository
        )
    }

    @Test
    fun getTrips_returnsMappedDomainTrips() = runTest {
        val dbTrips = listOf(
            DbTrip(1, 1, "Paris", "01/01/2026", "05/01/2026", 100.0, hashMapOf(), hashMapOf(), hashMapOf(), hashMapOf(), LinkedList(), "Desc")
        )
        every { database.getTrips(1) } returns flowOf(dbTrips)

        val result = repository.getTrips().first()

        assertEquals(1, result.size)
        assertEquals("Paris", result[0].title)
        verify { database.getTrips(1) }
    }

    @Test
    fun addTrip_persistsToDatabase() = runTest {
        val newTrip = Trip(0, "Rome", "10/10/2026", "15/10/2026", 500.0, hashMapOf(), hashMapOf(), hashMapOf(), hashMapOf(), LinkedList(), "Desc")
        every { database.insertTrip(any()) } returns 123L

        val result = repository.addTrip(newTrip)

        assertEquals(123, result.id)
        verify { database.insertTrip(match { it.title == "Rome" && it.userId == 1 }) }
    }

    @Test
    fun updateTrip_returnsTrueIfSuccess() = runTest {
        val trip = Trip(123, "Updated Rome", "10/10/2026", "15/10/2026", 500.0, hashMapOf(), hashMapOf(), hashMapOf(), hashMapOf(), LinkedList(), "Desc")
        every { database.getTripByIdSync(1, 123) } returns mockk()
        every { database.insertTrip(any()) } returns 123L

        val success = repository.updateTrip(trip)

        assertTrue(success)
        verify { database.insertTrip(match { it.title == "Updated Rome" }) }
    }

    @Test
    fun deleteTrip_callsDatabase() = runTest {
        every { database.deleteTrip(1, 123) } returns 1

        val success = repository.deleteTrip(123)

        assertTrue(success)
        verify { database.deleteTrip(1, 123) }
    }
}

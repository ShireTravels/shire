package com.example.shire.domain.repository

import com.example.shire.db.dbImpl
import com.example.shire.db.Activity as DbActivity
import com.example.shire.domain.model.Activity
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ActivityRepositoryTest {

    private lateinit var repository: ActivityRepositoryImpl
    private val database: dbImpl = mockk(relaxed = true)

    @Before
    fun setUp() {
        repository = ActivityRepositoryImpl(database)
    }

    @Test
    fun getActivitiesForTrip_returnsMappedList() = runTest {
        val dbActivities = listOf(
            DbActivity(1, 10, "Visit Louvre", "Museum", LocalDate.now(), LocalTime.now(), 20.0)
        )
        every { database.getActivitiesByTrip(10) } returns flowOf(dbActivities)

        val result = repository.getActivitiesForTrip(10).first()

        assertEquals(1, result.size)
        assertEquals("Visit Louvre", result[0].title)
        verify { database.getActivitiesByTrip(10) }
    }

    @Test
    fun addActivity_persistsToDatabase() = runTest {
        val activity = Activity(0, 10, "Dinner", "Italian", LocalDate.now(), LocalTime.now(), 50.0)
        every { database.insertActivity(any()) } returns 456L

        val result = repository.addActivity(activity)

        assertEquals(456, result.id)
        verify { database.insertActivity(match { it.title == "Dinner" && it.tripId == 10 }) }
    }

    @Test
    fun deleteActivity_callsDatabase() = runTest {
        every { database.deleteActivity(789) } returns 1

        val success = repository.deleteActivity(789)

        assertTrue(success)
        verify { database.deleteActivity(789) }
    }
}

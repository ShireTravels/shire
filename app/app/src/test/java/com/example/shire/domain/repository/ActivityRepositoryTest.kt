package com.example.shire.domain.repository

import android.content.Context
import com.example.shire.db.dbImpl
import com.example.shire.db.Activity as DbActivity
import com.example.shire.domain.model.Activity
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first

class ActivityRepositoryTest {

    private lateinit var repository: ActivityRepositoryImpl
    private lateinit var mockDb: dbImpl
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockDb = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)

        every { mockDb.getActivityById(501) } returns DbActivity(
            id = 501,
            tripId = 1,
            title = "Museum Tour",
            description = "Visit the Louvre",
            date = LocalDate.now(),
            time = LocalTime.NOON,
            price = 15.0
        )

        every { mockDb.getActivitiesByTrip(1) } returns flowOf(emptyList())
        every { mockDb.insertActivity(any()) } returns 1L

        repository = ActivityRepositoryImpl(mockDb)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getActivity returns mapped domain object`() = runBlocking {
        val activity = repository.getActivity(501)
        assertEquals("Museum Tour", activity?.title)
    }
}

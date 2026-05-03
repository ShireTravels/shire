package com.example.shire.domain.repository

import android.content.Context
import com.example.shire.db.dbImpl
import com.example.shire.db.Place as DbPlace
import com.example.shire.domain.model.Place
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.Date

class PlaceRepositoryTest {

    private lateinit var repository: PlaceRepositoryImpl
    private lateinit var mockDb: dbImpl
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockDb = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)

        every { mockDb.getPlaceById(401) } returns DbPlace(
            id = 401,
            name = "Eiffel Tower",
            location = "Paris",
            type = "Monument",
            rating = 4.9f,
            imageUrl = "",
            openHour = Date(),
            closeHour = Date(),
            price = 25.0
        )

        every { mockDb.getPlaces() } returns emptyList()
        every { mockDb.insertPlace(any()) } returns 1L

        repository = PlaceRepositoryImpl(mockDb)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getPlace returns mapped domain object`() {
        val place = repository.getPlace(401)
        assertEquals("Eiffel Tower", place.name)
    }
}

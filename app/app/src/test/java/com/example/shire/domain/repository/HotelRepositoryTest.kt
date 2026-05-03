package com.example.shire.domain.repository

import android.content.Context
import com.example.shire.db.dbImpl
import com.example.shire.db.Hotel as DbHotel
import com.example.shire.domain.model.Hotel
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HotelRepositoryTest {

    private lateinit var repository: HotelRepositoryImpl
    private lateinit var mockDb: dbImpl
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockkStatic("com.example.shire.db.DbKt")
        mockDb = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)

        every { com.example.shire.db.db(any()) } returns mockDb

        // Return a predefined hotel when requested
        every { mockDb.getHotelById(101) } returns DbHotel(
            id = 101,
            name = "Le Meurice Mock",
            location = "Paris",
            rating = 4.8f,
            imageUrl = "",
            amenities = listOf("WiFi", "Pool"),
            description = "Mock description",
            price = 300.0
        )

        // Mock getHotels for the init block's seedIfNeeded
        every { mockDb.getHotels() } returns emptyList()
        every { mockDb.insertHotel(any()) } returns 1L

        repository = HotelRepositoryImpl(mockContext)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getHotel returns mapped domain object`() {
        val hotel = repository.getHotel(101)
        assertEquals("Le Meurice Mock", hotel.name)
    }
}

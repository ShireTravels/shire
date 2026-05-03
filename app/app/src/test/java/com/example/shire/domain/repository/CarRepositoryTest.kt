package com.example.shire.domain.repository

import android.content.Context
import com.example.shire.db.dbImpl
import com.example.shire.db.Car as DbCar
import com.example.shire.domain.model.Car
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CarRepositoryTest {

    private lateinit var repository: CarRepositoryImpl
    private lateinit var mockDb: dbImpl
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        mockDb = mockk(relaxed = true)
        mockContext = mockk(relaxed = true)

        every { mockDb.getCarById(201) } returns DbCar(
            id = 201,
            model = "Tesla Model 3",
            type = "Electric",
            pricePerDay = 50.0,
            imageUrl = "",
            transmission = "Auto",
            seats = 5,
            features = listOf("AC", "GPS")
        )

        every { mockDb.getCars() } returns emptyList()
        every { mockDb.insertCar(any()) } returns 1L

        repository = CarRepositoryImpl(mockDb)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `test getCar returns mapped domain object`() {
        val car = repository.getCar(201)
        assertEquals("Tesla Model 3", car.model)
    }
}

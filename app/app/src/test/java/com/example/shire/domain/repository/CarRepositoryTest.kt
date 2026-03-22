package com.example.shire.domain.repository

import com.example.shire.domain.model.Car
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CarRepositoryTest {
    private lateinit var carRepository: CarRepositoryImpl

    @Before
    fun setUp() {
        carRepository = CarRepositoryImpl()
    }

    @Test
    fun testGetCars_returnsInitialList() {
        val cars = carRepository.getCars()
        assertTrue(cars.isNotEmpty())
    }

    @Test
    fun testGetCar_existingId_returnsCar() {
        val car = carRepository.getCar(301)
        assertNotNull(car)
        assertEquals("Peugeot 208", car.model)
    }

    @Test
    fun testAddCar_addsToList() {
        val initialSize = carRepository.getCars().size
        val newCar = Car(id = 999, model = "Test Model", type = "Sedan", pricePerDay = 50.0, imageUrl = "", transmission = "Auto", seats = 4, features = emptyList())
        
        carRepository.addCar(newCar)
        assertEquals(initialSize + 1, carRepository.getCars().size)
    }

    @Test
    fun testDeleteCar_removesFromList() {
        val initialSize = carRepository.getCars().size
        val success = carRepository.deleteCar(301)
        
        assertTrue(success)
        assertEquals(initialSize - 1, carRepository.getCars().size)
    }

    @Test
    fun testUpdateCar_modifiesExistingCar() {
        val existing = carRepository.getCar(301)
        val updated = existing.copy(model = "Updated Model")
        
        val success = carRepository.updateCar(updated)
        assertTrue(success)
        assertEquals("Updated Model", carRepository.getCar(301).model)
    }
}

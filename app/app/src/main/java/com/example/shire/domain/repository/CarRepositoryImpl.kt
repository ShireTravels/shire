package com.example.shire.domain.repository

import com.example.shire.domain.model.Car
import com.example.shire.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepositoryImpl @Inject constructor() : CarRepository {

    private val cars = mutableListOf<Car>()

    init {
        cars.add(
            Car(
                id = 301,
                model = "Peugeot 208",
                type = "Compact",
                pricePerDay = 45.0,
                imageUrl = "https://example.com/peugeot.jpg",
                transmission = "Manual",
                seats = 5,
                features = listOf("AC", "Bluetooth")
            )
        )
    }

    override fun getCar(carId: Int): Car {
        return cars.first { it.id == carId }
    }

    override fun getCars(): List<Car> {
        return cars
    }

    override fun getUserCars(user: User): List<Car> {
        return cars
    }

    override fun addCar(car: Car): Car {
        cars.add(car)
        return car
    }

    override fun deleteCar(carId: Int): Boolean {
        return cars.removeAll { it.id == carId }
    }

    override fun updateCar(car: Car): Boolean {
        val index = cars.indexOfFirst { it.id == car.id }
        if (index != -1) {
            cars[index] = car
            return true
        }
        return false
    }
}
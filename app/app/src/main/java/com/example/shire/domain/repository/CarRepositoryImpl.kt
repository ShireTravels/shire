package com.example.shire.domain.repository

import com.example.shire.domain.model.Car
import com.example.shire.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

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
        Log.d("CarRepo", "Fetching car with id: $carId")
        return cars.first { it.id == carId }
    }

    override fun getCars(): List<Car> {
        Log.d("CarRepo", "Fetching all cars. Count: ${cars.size}")
        return cars
    }

    override fun getUserCars(user: User): List<Car> {
        Log.d("CarRepo", "Fetching cars for user: $user")
        return cars
    }

    override fun addCar(car: Car): Car {
        cars.add(car)
        Log.i("CarRepo", "Added new car: ${car.model} (ID: ${car.id})")
        return car
    }

    override fun deleteCar(carId: Int): Boolean {
        val removed = cars.removeAll { it.id == carId }
        if (removed) Log.i("CarRepo", "Deleted car successfully (ID: $carId)")
        else Log.e("CarRepo", "Failed to delete car: ID $carId not found")
        return removed
    }

    override fun updateCar(car: Car): Boolean {
        val index = cars.indexOfFirst { it.id == car.id }
        if (index != -1) {
            cars[index] = car
            Log.i("CarRepo", "Updated car successfully (ID: ${car.id})")
            return true
        }
        Log.e("CarRepo", "Failed to update car: ID ${car.id} not found")
        return false
    }
}

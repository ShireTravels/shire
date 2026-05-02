package com.example.shire.domain.repository

import android.content.Context
import android.util.Log
import com.example.shire.db.Car as DbCar
import com.example.shire.db.db
import com.example.shire.domain.model.Car
import com.example.shire.domain.model.User
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : CarRepository {

    private val database = db(context)

    private val seedCars = listOf(
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

    init {
        seedIfNeeded()
    }

    override fun getCar(carId: Int): Car {
        Log.d("CarRepo", "Fetching car with id: $carId")
        return database.getCarById(carId)?.toDomainCar()
            ?: throw NoSuchElementException("Car with id=$carId not found")
    }

    override fun getCars(): List<Car> {
        val cars = database.getCars().map { it.toDomainCar() }
        Log.d("CarRepo", "Fetching all cars. Count: ${cars.size}")
        return cars
    }

    override fun getUserCars(user: User): List<Car> {
        Log.d("CarRepo", "Fetching cars for user: $user")
        return getCars()
    }

    override fun addCar(car: Car): Car {
        val insertedId = database.insertCar(car.toDbCar()).toInt()
        val persistedCar = if (car.id > 0) car else car.copy(id = insertedId)
        Log.i("CarRepo", "Added new car: ${persistedCar.model} (ID: ${persistedCar.id})")
        return persistedCar
    }

    override fun deleteCar(carId: Int): Boolean {
        val removed = database.deleteCar(carId) > 0
        if (removed) Log.i("CarRepo", "Deleted car successfully (ID: $carId)")
        else Log.e("CarRepo", "Failed to delete car: ID $carId not found")
        return removed
    }

    override fun updateCar(car: Car): Boolean {
        val exists = database.getCarById(car.id) != null
        if (exists) {
            database.insertCar(car.toDbCar())
            Log.i("CarRepo", "Updated car successfully (ID: ${car.id})")
            return true
        }
        Log.e("CarRepo", "Failed to update car: ID ${car.id} not found")
        return false
    }

    private fun seedIfNeeded() {
        if (database.getCars().isNotEmpty()) return
        seedCars.forEach { car -> database.insertCar(car.toDbCar()) }
    }

    private fun Car.toDbCar(): DbCar = DbCar(
        id = id,
        model = model,
        type = type,
        pricePerDay = pricePerDay,
        imageUrl = imageUrl,
        transmission = transmission,
        seats = seats,
        features = features
    )

    private fun DbCar.toDomainCar(): Car = Car(
        id = id,
        model = model,
        type = type,
        pricePerDay = pricePerDay,
        imageUrl = imageUrl,
        transmission = transmission,
        seats = seats,
        features = features
    )
}

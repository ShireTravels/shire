package com.example.shire.domain.repository

import com.example.shire.domain.model.Car
import com.example.shire.domain.model.User

interface CarRepository {
    fun getCar(carId: Int): Car
    fun getCars(): List<Car>
    fun getUserCars(user: User): List<Car>
    fun addCar(car: Car): Car
    fun deleteCar(carId: Int): Boolean
    fun updateCar(car: Car): Boolean
}

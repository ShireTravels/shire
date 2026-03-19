package com.example.shire.domain.repository

import com.example.shire.domain.model.Car
import com.example.shire.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarRepositoryImpl @Inject constructor() : CarRepository {

    // Listas mutables para almacenar datos en memoria
    private val Cars = mutableListOf<Car>()
    override fun getCar(carId: Int): Car {
        TODO("Not yet implemented")
    }

    override fun getCars(): List<Car> {
        // Asignamos a cada Car las subtareas correspondientes
        return Cars.map { Car ->
            Car.copy(0)
        }
    }

    override fun getUserCars(user: User): List<Car> {
        TODO("Not yet implemented")
    }

    override fun addCar(Car: Car): Car {
        // Generamos un id simple basado en el tamaño actual
        val newCar = Car.copy(id = Cars.size + 1)
        Cars.add(newCar)
        return newCar
    }

    override fun deleteCar(carId: Int): Boolean {
        Cars.removeAll { it.id == carId }
        // También eliminamos sus subtareas
        return true
    }

    override fun updateCar(car: Car): Boolean {
        // Buscar la tarea por su id y actualizarla
        val index = Cars.indexOfFirst { it.id == car.id }
        if (index != -1) {
            Cars[index] = car
        }
        return true
    }
}
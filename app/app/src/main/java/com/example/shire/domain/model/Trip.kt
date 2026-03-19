package com.example.shire.domain.model

import java.util.LinkedList

data class Trip(
    var id: Int = 0,
    var hotel: HashMap<Int, Int>, //num dia / id hotel
    var vuelo: HashMap<Int, Int>,
    var coche: HashMap<Int, Int>,
    var places: HashMap<Int, Int>,
    var presupuesto: Double,
    var gallery: LinkedList<String>,
    var notas: String
)
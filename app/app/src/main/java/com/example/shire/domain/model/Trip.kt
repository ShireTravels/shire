package com.example.shire.domain.model

import java.util.LinkedList

data class Trip(
    var id: Int = 0, //ide del trip
    var title: String,
    var startDate: String, // format: dd/MM/yyyy
    var endDate: String,   // format: dd/MM/yyyy
    var price: Double,
    var hotel: HashMap<Int, Int>, //num dia / id hotel
    var flight: HashMap<Int, Int>,
    var car: HashMap<Int, Int>,
    var places: HashMap<Int, MutableList<Int>>,
    var gallery: LinkedList<String>,
    var description: String
)
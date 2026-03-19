package com.example.shire.domain.model

import java.util.LinkedList

data class Trip(
    var id: Int = 0, //ide del trip
    var title: String,
    var dates: String,
    var price: Double,
    var hotel: HashMap<Int, Int>, //num dia / id hotel
    var flight: HashMap<Int, Int>,
    var car: HashMap<Int, Int>,
    var places: HashMap<Int, Int>,
    var gallery: LinkedList<String>,
    var notes: String
)
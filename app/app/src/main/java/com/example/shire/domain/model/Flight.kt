package com.example.shire.domain.model

import java.util.Date

data class Flight(
    var id: Int = 0,
    var flightNumber: String,
    var company: String,
    var departureCity: String,
    var arrivalCity: String,
    var departureDate: Date,
    var arrivalDate: Date,
    var terminal: Int,
    var gate: Int,
    var type: String,
    var price: Double
)
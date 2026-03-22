package com.example.shire.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class Activity(
    val id: Int = 0,
    val tripId: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime,
    val price: Double = 0.0
)

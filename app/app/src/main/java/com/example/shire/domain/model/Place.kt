package com.example.shire.domain.model
import java.util.Date

data class Place(
    val id: Int = 0,
    val name: String,
    val location: String,
    val type: String,
    val rating: Float,
    val imageUrl: String,
    val openHour: Date,
    val closeHour: Date,
    val price: Double,
)
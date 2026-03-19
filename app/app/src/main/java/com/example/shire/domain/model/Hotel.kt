package com.example.shire.domain.model

data class Hotel(
    val id: Int = 0,
    val name: String,
    val location: String,
    val rating: Float,
    val imageUrl: String,
    val amenities: List<String>,
    val description: String,
    val price: Double
)
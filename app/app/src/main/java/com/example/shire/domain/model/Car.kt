package com.example.shire.domain.model

data class Car(
    var id: Int = 0,
    val model: String,
    val type: String,
    val pricePerDay: Double,
    val imageUrl: String,
    val transmission: String,
    val seats: Int,
    val features: List<String>
)
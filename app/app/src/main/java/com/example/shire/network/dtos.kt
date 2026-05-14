package com.example.shire.network

data class HotelDto(
    val id: Long,
    val name: String,
    val city: String,
    val address: String,
    val images: List<String> = emptyList(),
    val rooms: List<RoomDto> = emptyList()
)

data class HotelDetailsDto(
    val id: Long,
    val name: String,
    val description: String?,
    val address: String,
    val images: List<String> = emptyList(),
    val rooms: List<RoomDto> = emptyList()
)

data class RoomDto(
    val id: Long,
    val type: String,
    val price: Double,
    val images: List<String> = emptyList()
)

data class ReservationDto(
    val id: Long,
    val hotelId: Long,
    val roomId: Long,
    val startDate: String,
    val endDate: String,
    val userName: String,
    val price: Double
)

data class CreateReservationRequest(
    val hotelId: Long,
    val roomId: Long,
    val startDate: String,
    val endDate: String,
    val userName: String
)

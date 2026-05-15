package com.example.shire.network

import com.google.gson.annotations.SerializedName

data class HotelDto(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<RoomDto> = emptyList(),
    @SerializedName("image_url") val imageUrl: String
)

data class HotelDetailsDto(
    val id: String,
    val name: String,
    val address: String,
    val rating: Int,
    val rooms: List<RoomDto> = emptyList(),
    @SerializedName("image_url") val imageUrl: String
)

data class RoomDto(
    val id: String,
    @SerializedName("room_type") val roomType: String,
    val price: Double,
    val images: List<String> = emptyList()
)

data class ReservationDto(
    val id: String? = null,
    @SerializedName("hotel_id") val hotelId: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("guest_name") val guestName: String,
    @SerializedName("guest_email") val guestEmail: String
)

data class CreateReservationRequest(
    @SerializedName("hotel_id") val hotelId: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("guest_name") val guestName: String,
    @SerializedName("guest_email") val guestEmail: String
)

data class ReserveRequest(
    @SerializedName("hotel_id") val hotelId: String,
    @SerializedName("room_id") val roomId: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("guest_name") val guestName: String,
    @SerializedName("guest_email") val guestEmail: String
)

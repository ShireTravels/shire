package com.example.shire.domain.repository

import com.example.shire.network.HotelApi
import com.example.shire.network.HotelDto
import com.example.shire.network.ReservationDto
import com.example.shire.network.ReserveRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HotelRemoteRepository @Inject constructor(
    private val hotelApi: HotelApi
) {
    suspend fun listHotels(groupId: String): List<HotelDto> =
        hotelApi.listHotels(groupId = groupId)

    suspend fun checkAvailability(
        groupId: String,
        startDate: String,
        endDate: String,
        hotelId: String? = null,
        city: String? = null
    ): List<HotelDto> = hotelApi.checkAvailability(
        groupId = groupId,
        startDate = startDate,
        endDate = endDate,
        hotelId = hotelId,
        city = city
    )

    suspend fun reserveRoom(groupId: String, request: ReserveRequest): ReservationDto =
        hotelApi.reserveRoom(groupId = groupId, request = request)

    suspend fun cancelGroupReservation(groupId: String, request: ReserveRequest): ReservationDto =
        hotelApi.cancelHotelReservation(groupId = groupId, request = request)

    suspend fun listReservations(groupId: String, guestEmail: String? = null): List<ReservationDto> =
        hotelApi.listReservations(groupId = groupId, guestEmail = guestEmail)

    suspend fun listAllReservations(): List<ReservationDto> =
        hotelApi.listAllReservations()

    suspend fun getReservationById(resId: String): ReservationDto =
        hotelApi.getReservationById(resId = resId)

    suspend fun cancelReservation(resId: String) {
        hotelApi.cancelReservation(resId = resId)
    }
}

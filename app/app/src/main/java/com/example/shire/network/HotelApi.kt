package com.example.shire.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApi {
    @GET("/hotels/{group_id}/hotels")
    suspend fun listHotels(
        @Path("group_id") groupId: String
    ): List<HotelDto>

    @GET("/hotels/{group_id}/availability")
    suspend fun checkAvailability(
        @Path("group_id") groupId: String,
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("hotel_id") hotelId: String? = null,
        @Query("city") city: String? = null
    ): List<HotelDto>

    @POST("/hotels/{group_id}/reserve")
    suspend fun reserveRoom(
        @Path("group_id") groupId: String,
        @Body request: ReserveRequest
    ): ReservationDto

    @POST("/hotels/{group_id}/cancel")
    suspend fun cancelHotelReservation(
        @Path("group_id") groupId: String,
        @Body request: ReserveRequest
    ): ReservationDto

    @GET("/hotels/{group_id}/reservations")
    suspend fun listReservations(
        @Path("group_id") groupId: String,
        @Query("guest_email") guestEmail: String? = null
    ): List<ReservationDto>

    @GET("/reservations")
    suspend fun listAllReservations(): List<ReservationDto>

    @GET("/reservations/{res_id}")
    suspend fun getReservationById(
        @Path("res_id") resId: String
    ): ReservationDto

    @DELETE("/reservations/{res_id}")
    suspend fun cancelReservation(
        @Path("res_id") resId: String
    )
}

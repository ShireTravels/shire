package com.example.shire.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface HotelApi {
    @GET("/api/hotels/availability")
    suspend fun searchHotels(
        @Query("city") city: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): List<HotelDto>

    @GET("/api/hotels/{hotelId}")
    suspend fun getHotelDetails(
        @Path("hotelId") hotelId: Long
    ): HotelDetailsDto

    @POST("/api/reservations")
    suspend fun createReservation(
        @Body request: CreateReservationRequest
    ): ReservationDto

    @GET("/api/reservations")
    suspend fun listReservations(
        @Query("userId") userId: Long
    ): List<ReservationDto>

    @DELETE("/api/reservations/{reservationId}")
    suspend fun cancelReservation(
        @Path("reservationId") reservationId: Long
    )
}

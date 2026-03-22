package com.example.shire.di

import com.example.shire.domain.repository.CarRepository
import com.example.shire.domain.repository.CarRepositoryImpl
import com.example.shire.domain.repository.FlightRepository
import com.example.shire.domain.repository.FlightRepositoryImpl
import com.example.shire.domain.repository.HotelRepository
import com.example.shire.domain.repository.HotelRepositoryImpl
import com.example.shire.domain.repository.PlaceRepository
import com.example.shire.domain.repository.PlaceRepositoryImpl
import com.example.shire.domain.repository.ProfilePreferencesRepository
import com.example.shire.domain.repository.ProfilePreferencesRepositoryImpl
import com.example.shire.domain.repository.TripRepository
import com.example.shire.domain.repository.TripRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//cuando un ViewModel pide la interfaz, Hilt sabe qué debe inyectar

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepositoryImpl: TripRepositoryImpl
    ): TripRepository

    @Binds
    @Singleton
    abstract fun bindHotelRepository(
        hotelRepositoryImpl: HotelRepositoryImpl
    ): HotelRepository

    @Binds
    @Singleton
    abstract fun bindFlightRepository(
        flightRepositoryImpl: FlightRepositoryImpl
    ): FlightRepository

    @Binds
    @Singleton
    abstract fun bindCarRepository(
        carRepositoryImpl: CarRepositoryImpl
    ): CarRepository

    @Binds
    @Singleton
    abstract fun bindPlaceRepository(
        placeRepositoryImpl: PlaceRepositoryImpl
    ): PlaceRepository

    @Binds
    @Singleton
    abstract fun bindProfilePreferencesRepository(
        profilePreferencesRepository: ProfilePreferencesRepositoryImpl
    ): ProfilePreferencesRepository
}


package com.example.shire.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        User::class,
        Activity::class,
        Car::class,
        Flight::class,
        Hotel::class,
        Place::class,
        Preferences::class,
        Trip::class
    ],
    version = 5,
    exportSchema = false
)
@TypeConverters(ShireTypeConverters::class)
abstract class ShireRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun activityDao(): ActivityDao
    abstract fun carDao(): CarDao
    abstract fun flightDao(): FlightDao
    abstract fun hotelDao(): HotelDao
    abstract fun placeDao(): PlaceDao
    abstract fun preferencesDao(): PreferencesDao
    abstract fun tripDao(): TripDao
}

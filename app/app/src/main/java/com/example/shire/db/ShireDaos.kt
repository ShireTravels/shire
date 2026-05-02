package com.example.shire.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(user: User): Long

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun getById(id: Int): User?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    fun getByEmail(email: String): User?
}

@Dao
interface ActivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activity: Activity): Long

    @Query("SELECT * FROM activities WHERE id = :activityId LIMIT 1")
    fun getById(activityId: Int): Activity?

    @Query("SELECT * FROM activities WHERE trip_id = :tripId ORDER BY date ASC, time ASC")
    fun getByTripId(tripId: Int): List<Activity>

    @Query("DELETE FROM activities WHERE id = :activityId")
    fun deleteById(activityId: Int): Int
}

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(car: Car): Long

    @Query("SELECT * FROM cars WHERE id = :carId LIMIT 1")
    fun getById(carId: Int): Car?

    @Query("SELECT * FROM cars")
    fun getAll(): List<Car>

    @Query("DELETE FROM cars WHERE id = :carId")
    fun deleteById(carId: Int): Int
}

@Dao
interface FlightDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(flight: Flight): Long

    @Query("SELECT * FROM flights WHERE id = :flightId LIMIT 1")
    fun getById(flightId: Int): Flight?

    @Query("SELECT * FROM flights")
    fun getAll(): List<Flight>

    @Query("DELETE FROM flights WHERE id = :flightId")
    fun deleteById(flightId: Int): Int
}

@Dao
interface HotelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(hotel: Hotel): Long

    @Query("SELECT * FROM hotels WHERE id = :hotelId LIMIT 1")
    fun getById(hotelId: Int): Hotel?

    @Query("SELECT * FROM hotels")
    fun getAll(): List<Hotel>

    @Query("DELETE FROM hotels WHERE id = :hotelId")
    fun deleteById(hotelId: Int): Int
}

@Dao
interface PlaceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: Place): Long

    @Query("SELECT * FROM places WHERE id = :placeId LIMIT 1")
    fun getById(placeId: Int): Place?

    @Query("SELECT * FROM places")
    fun getAll(): List<Place>

    @Query("DELETE FROM places WHERE id = :placeId")
    fun deleteById(placeId: Int): Int
}

@Dao
interface PreferencesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(preferences: Preferences): Long

    @Query("SELECT * FROM preferences WHERE user_id = :userId LIMIT 1")
    fun getByUserId(userId: Int): Preferences?
}

@Dao
interface TripDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(trip: Trip): Long

    @Query("SELECT * FROM trips WHERE user_id = :userId ORDER BY start_date ASC")
    fun getByUserId(userId: Int): List<Trip>

    @Query("SELECT * FROM trips WHERE user_id = :userId AND id = :id LIMIT 1")
    fun getByUserIdAndTripId(userId: Int, id: Int): Trip?

    @Query("DELETE FROM trips WHERE user_id = :userId AND id = :tripId")
    fun deleteByUserIdAndTripId(userId: Int, tripId: Int): Int
}

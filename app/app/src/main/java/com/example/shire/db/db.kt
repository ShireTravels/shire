package com.example.shire.db

import android.content.Context
import androidx.room.Room

class db(context: Context) : dbImpl {

	private val roomDb: ShireRoomDatabase = getOrCreateRoomDatabase(context.applicationContext)

	override fun upsertUser(user: User): Long = roomDb.userDao().upsert(user)

	override fun getUserById(id: Int): User? = roomDb.userDao().getById(id)

	override fun getUserByEmail(email: String): User? = roomDb.userDao().getByEmail(email)

	override fun insertActivity(activity: Activity): Long = roomDb.activityDao().insert(activity)

	override fun getActivityById(id: Int): Activity? = roomDb.activityDao().getById(id)

	override fun getActivitiesByTrip(tripId: Int): List<Activity> = roomDb.activityDao().getByTripId(tripId)

	override fun deleteActivity(id: Int): Int = roomDb.activityDao().deleteById(id)

	override fun insertCar(car: Car): Long = roomDb.carDao().insert(car)

	override fun getCarById(id: Int): Car? = roomDb.carDao().getById(id)

	override fun getCars(): List<Car> = roomDb.carDao().getAll()

	override fun deleteCar(id: Int): Int = roomDb.carDao().deleteById(id)

	override fun insertFlight(flight: Flight): Long = roomDb.flightDao().insert(flight)

	override fun getFlightById(id: Int): Flight? = roomDb.flightDao().getById(id)

	override fun getFlights(): List<Flight> = roomDb.flightDao().getAll()

	override fun deleteFlight(id: Int): Int = roomDb.flightDao().deleteById(id)

	override fun insertHotel(hotel: Hotel): Long = roomDb.hotelDao().insert(hotel)

	override fun getHotelById(id: Int): Hotel? = roomDb.hotelDao().getById(id)

	override fun getHotels(): List<Hotel> = roomDb.hotelDao().getAll()

	override fun deleteHotel(id: Int): Int = roomDb.hotelDao().deleteById(id)

	override fun insertPlace(place: Place): Long = roomDb.placeDao().insert(place)

	override fun getPlaceById(id: Int): Place? = roomDb.placeDao().getById(id)

	override fun getPlaces(): List<Place> = roomDb.placeDao().getAll()

	override fun deletePlace(id: Int): Int = roomDb.placeDao().deleteById(id)

	override fun savePreferences(preferences: Preferences): Long = roomDb.preferencesDao().upsert(preferences)

	override fun getPreferences(userId: Int): Preferences? = roomDb.preferencesDao().getByUserId(userId)

	override fun insertTrip(trip: Trip): Long = roomDb.tripDao().insert(trip)

	override fun getTrips(userId: Int): List<Trip> = roomDb.tripDao().getByUserId(userId)

	override fun getTripById(userId: Int, id: Int): Trip? = roomDb.tripDao().getByUserIdAndTripId(userId, id)

	override fun deleteTrip(userId: Int, id: Int): Int = roomDb.tripDao().deleteByUserIdAndTripId(userId, id)

	override fun closeDb() {
		synchronized(db::class.java) {
			roomDatabase?.close()
			roomDatabase = null
		}
	}

	companion object {
		private const val DB_NAME = "shire.db"
		@Volatile
		private var roomDatabase: ShireRoomDatabase? = null

		private fun getOrCreateRoomDatabase(context: Context): ShireRoomDatabase {
			return roomDatabase ?: synchronized(this) {
				roomDatabase ?: Room.databaseBuilder(
					context,
					ShireRoomDatabase::class.java,
					DB_NAME
				)
					.fallbackToDestructiveMigration()
					.allowMainThreadQueries()
					.build()
					.also { roomDatabase = it }
			}
		}

		fun initialize(context: Context) {
			getOrCreateRoomDatabase(context.applicationContext).openHelper.writableDatabase
		}
	}
}
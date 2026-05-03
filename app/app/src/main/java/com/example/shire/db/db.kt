package com.example.shire.db

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.io.IOException
import kotlinx.coroutines.flow.Flow

class db(context: Context) : dbImpl {

	private val roomDb: ShireRoomDatabase = getOrCreateRoomDatabase(context.applicationContext)

	override fun upsertUser(user: User): Long = roomDb.userDao().upsert(user).also { Log.i("ShireDB", "Upserted user: ${user.email} (ID: $it)") }

	override fun getUserById(id: Int): Flow<User?> = roomDb.userDao().getById(id)
	override fun getUserByIdSync(id: Int): User? = roomDb.userDao().getByIdSync(id)

	override fun getUserByEmail(email: String): User? = roomDb.userDao().getByEmail(email)
	override fun getUserByUsername(username: String): Flow<User?> = roomDb.userDao().getByUsername(username)
	override fun getUserByUsernameSync(username: String): User? = roomDb.userDao().getByUsernameSync(username)

	override fun insertActivity(activity: Activity): Long = roomDb.activityDao().insert(activity).also { Log.i("ShireDB", "Inserted activity: ${activity.title} (ID: $it)") }

	override fun getActivityById(id: Int): Activity? = roomDb.activityDao().getById(id)

	override fun getActivitiesByTrip(tripId: Int): Flow<List<Activity>> = roomDb.activityDao().getByTripId(tripId)
	override fun getActivitiesByTripSync(tripId: Int): List<Activity> = roomDb.activityDao().getByTripIdSync(tripId)

	override fun deleteActivity(id: Int): Int = roomDb.activityDao().deleteById(id).also { Log.i("ShireDB", "Deleted activity ID: $id (Rows: $it)") }

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

	override fun savePreferences(preferences: Preferences): Long = roomDb.preferencesDao().upsert(preferences).also { Log.i("ShireDB", "Saved preferences for user ID: ${preferences.userId}") }

	override fun getPreferences(userId: Int): Preferences? = roomDb.preferencesDao().getByUserId(userId)

	override fun insertTrip(trip: Trip): Long = roomDb.tripDao().insert(trip).also { Log.i("ShireDB", "Inserted trip: ${trip.title} (ID: $it)") }

	override fun getTrips(userId: Int): Flow<List<Trip>> = roomDb.tripDao().getByUserId(userId)
	override fun getTripsSync(userId: Int): List<Trip> = roomDb.tripDao().getByUserIdSync(userId)

	override fun getTripById(userId: Int, id: Int): Flow<Trip?> = roomDb.tripDao().getByUserIdAndTripId(userId, id)
	override fun getTripByIdSync(userId: Int, id: Int): Trip? = roomDb.tripDao().getByUserIdAndTripIdSync(userId, id)
	override fun getTripByTitleSync(userId: Int, title: String): Trip? = roomDb.tripDao().getByUserIdAndTitleSync(userId, title)

	override fun deleteTrip(userId: Int, id: Int): Int = roomDb.tripDao().deleteByUserIdAndTripId(userId, id).also { Log.i("ShireDB", "Deleted trip ID: $id for user $userId (Rows: $it)") }

	override fun insertAccessLog(log: AccessLog): Long = roomDb.accessLogDao().insert(log).also { Log.v("ShireDB", "Access log: ${log.action} for user ${log.userId}") }

	override fun getAccessLogs(userId: Int): List<AccessLog> = roomDb.accessLogDao().getByUserId(userId)

	override fun closeDb() {
		synchronized(db::class.java) {
			roomDatabase?.close()
			roomDatabase = null
		}
	}

	companion object {
		private const val DB_NAME = "shire.sqlite"
		private const val LEGACY_DB_NAME = "shire.db"
		private val MIGRATION_4_5 = object : Migration(4, 5) {
			override fun migrate(db: SupportSQLiteDatabase) {
				// No schema changes; this migration exists to avoid destructive fallback wipes.
			}
		}
		@Volatile
		private var roomDatabase: ShireRoomDatabase? = null

		private fun getOrCreateRoomDatabase(context: Context): ShireRoomDatabase {
			migrateLegacyDatabaseFileIfNeeded(context)

			return roomDatabase ?: synchronized(this) {
				roomDatabase ?: Room.databaseBuilder(
					context,
					ShireRoomDatabase::class.java,
					DB_NAME
				)
					.addMigrations(MIGRATION_4_5)
					.build()
					.also { roomDatabase = it }
			}
		}

		private fun migrateLegacyDatabaseFileIfNeeded(context: Context) {
			val newDb = context.getDatabasePath(DB_NAME)
			if (newDb.exists()) return

			val legacyDb = context.getDatabasePath(LEGACY_DB_NAME)
			if (!legacyDb.exists()) return

			newDb.parentFile?.mkdirs()
			try {
				legacyDb.copyTo(newDb, overwrite = false)

				copySidecarIfExists(
					from = context.getDatabasePath("$LEGACY_DB_NAME-wal"),
					to = context.getDatabasePath("$DB_NAME-wal")
				)
				copySidecarIfExists(
					from = context.getDatabasePath("$LEGACY_DB_NAME-shm"),
					to = context.getDatabasePath("$DB_NAME-shm")
				)
			} catch (_: IOException) {
				// If copy fails, Room will create a fresh DB file with the new name.
			}
		}

		private fun copySidecarIfExists(from: java.io.File, to: java.io.File) {
			if (!from.exists() || to.exists()) return
			try {
				from.copyTo(to, overwrite = false)
			} catch (_: IOException) {
				// Ignore sidecar copy errors; primary DB copy is enough for fallback.
			}
		}

		fun initialize(context: Context) {
			getOrCreateRoomDatabase(context.applicationContext).openHelper.writableDatabase
		}
	}
}
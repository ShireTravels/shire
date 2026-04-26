package com.example.shire.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class db(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), dbImpl {

	override fun onCreate(database: SQLiteDatabase) {
		ALL_DB_TABLES.forEach { table ->
			database.execSQL(table.createSql)
		}
	}

	override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		ALL_DB_TABLES.forEach { table ->
			database.execSQL(table.dropSql)
		}
		onCreate(database)
	}

	override fun insertActivity(activity: Activity): Long {
		return writableDatabase.insert(ActivityTable.tableName, null, ActivityTable.toContentValues(activity))
	}

	override fun getActivitiesByTrip(tripId: Int): List<Activity> {
		val result = mutableListOf<Activity>()
		val cursor = readableDatabase.query(
			ActivityTable.tableName,
			null,
			"trip_id = ?",
			arrayOf(tripId.toString()),
			null,
			null,
			"date ASC, time ASC"
		)

		cursor.use {
			while (it.moveToNext()) {
				result.add(ActivityTable.fromCursor(it))
			}
		}

		return result
	}

	override fun insertCar(car: Car): Long {
		return writableDatabase.insert(CarTable.tableName, null, CarTable.toContentValues(car))
	}

	override fun getCars(): List<Car> = queryAll(CarTable)

	override fun insertFlight(flight: Flight): Long {
		return writableDatabase.insert(FlightTable.tableName, null, FlightTable.toContentValues(flight))
	}

	override fun getFlights(): List<Flight> = queryAll(FlightTable)

	override fun insertHotel(hotel: Hotel): Long {
		return writableDatabase.insert(HotelTable.tableName, null, HotelTable.toContentValues(hotel))
	}

	override fun getHotels(): List<Hotel> = queryAll(HotelTable)

	override fun insertPlace(place: Place): Long {
		return writableDatabase.insert(PlaceTable.tableName, null, PlaceTable.toContentValues(place))
	}

	override fun getPlaces(): List<Place> = queryAll(PlaceTable)

	override fun savePreferences(preferences: Preferences): Long {
		return writableDatabase.insertWithOnConflict(
			PreferencesTable.tableName,
			null,
			PreferencesTable.toContentValues(preferences),
			SQLiteDatabase.CONFLICT_REPLACE
		)
	}

	override fun getPreferences(): Preferences? {
		val cursor = readableDatabase.query(
			PreferencesTable.tableName,
			null,
			"id = 1",
			null,
			null,
			null,
			null,
			"1"
		)

		cursor.use {
			return if (it.moveToFirst()) PreferencesTable.fromCursor(it) else null
		}
	}

	override fun insertTrip(trip: Trip): Long {
		return writableDatabase.insert(TripTable.tableName, null, TripTable.toContentValues(trip))
	}

	override fun getTrips(): List<Trip> {
		return queryAll(TripTable, orderBy = "start_date ASC")
	}

	override fun getTripById(id: Int): Trip? {
		val cursor = readableDatabase.query(
			TripTable.tableName,
			null,
			"id = ?",
			arrayOf(id.toString()),
			null,
			null,
			null,
			"1"
		)

		cursor.use {
			return if (it.moveToFirst()) TripTable.fromCursor(it) else null
		}
	}

	override fun closeDb() {
		close()
	}

	private fun <T> queryAll(table: DbTable<T>, orderBy: String? = null): List<T> {
		val result = mutableListOf<T>()
		val cursor = readableDatabase.query(
			table.tableName,
			null,
			null,
			null,
			null,
			null,
			orderBy
		)

		cursor.use {
			while (it.moveToNext()) {
				result.add(table.fromCursor(it))
			}
		}

		return result
	}

	companion object {
		private const val DB_NAME = "shire.db"
		private const val DB_VERSION = 1
	}
}
package com.example.shire.db

import android.content.ContentValues
import android.database.Cursor
import com.example.shire.domain.model.CurrencyOption
import com.example.shire.domain.model.DateFormatOption
import com.example.shire.domain.model.LanguageOption
import com.example.shire.domain.model.TextSizeOption
import com.example.shire.domain.model.ThemeOption
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date
import java.util.LinkedList

interface dbImpl {
    fun insertActivity(activity: Activity): Long
    fun getActivitiesByTrip(tripId: Int): List<Activity>

    fun insertCar(car: Car): Long
    fun getCars(): List<Car>

    fun insertFlight(flight: Flight): Long
    fun getFlights(): List<Flight>

    fun insertHotel(hotel: Hotel): Long
    fun getHotels(): List<Hotel>

    fun insertPlace(place: Place): Long
    fun getPlaces(): List<Place>

    fun savePreferences(preferences: Preferences): Long
    fun getPreferences(): Preferences?

    fun insertTrip(trip: Trip): Long
    fun getTrips(): List<Trip>
    fun getTripById(id: Int): Trip?

    fun closeDb()
}

interface DbTable<T> {
    val tableName: String
    val createSql: String
    val dropSql: String

    fun toContentValues(item: T): ContentValues
    fun fromCursor(cursor: Cursor): T
}

abstract class BaseDbTable<T>(
    final override val tableName: String,
    final override val createSql: String,
) : DbTable<T> {
    final override val dropSql: String = "DROP TABLE IF EXISTS $tableName"


object DbCodec {
    private const val ENTRY_SEPARATOR = "|"
    private const val KV_SEPARATOR = ":"
    private const val LIST_SEPARATOR = ","

    fun encodeStringList(values: List<String>): String = values.joinToString(ENTRY_SEPARATOR)

    fun decodeStringList(raw: String): List<String> {
        if (raw.isBlank()) return emptyList()
        return raw.split(ENTRY_SEPARATOR).map { it.trim() }.filter { it.isNotEmpty() }
    }

    fun encodeIntMap(values: Map<Int, Int>): String = values.entries.joinToString(ENTRY_SEPARATOR) {
        "${it.key}$KV_SEPARATOR${it.value}"
    }

    fun decodeIntMap(raw: String): HashMap<Int, Int> {
        val result = HashMap<Int, Int>()
        if (raw.isBlank()) return result

        raw.split(ENTRY_SEPARATOR).forEach { token ->
            val parts = token.split(KV_SEPARATOR)
            if (parts.size == 2) {
                val key = parts[0].toIntOrNull()
                val value = parts[1].toIntOrNull()
                if (key != null && value != null) result[key] = value
            }
        }

        return result
    }

    fun encodeIntListMap(values: Map<Int, MutableList<Int>>): String {
        return values.entries.joinToString(ENTRY_SEPARATOR) { entry ->
            val encodedList = entry.value.joinToString(LIST_SEPARATOR)
            "${entry.key}$KV_SEPARATOR$encodedList"
        }
    }

    fun decodeIntListMap(raw: String): HashMap<Int, MutableList<Int>> {
        val result = HashMap<Int, MutableList<Int>>()
        if (raw.isBlank()) return result

        raw.split(ENTRY_SEPARATOR).forEach { token ->
            val parts = token.split(KV_SEPARATOR)
            if (parts.size == 2) {
                val key = parts[0].toIntOrNull()
                if (key != null) {
                    val list = parts[1]
                        .split(LIST_SEPARATOR)
                        .mapNotNull { it.toIntOrNull() }
                        .toMutableList()
                    result[key] = list
                }
            }
        }

        return result
    }
}
}

data class Activity(
    val id: Int = 0,
    val tripId: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime,
    val price: Double = 0.0
)

data class Car(
    var id: Int = 0,
    val model: String,
    val type: String,
    val pricePerDay: Double,
    val imageUrl: String,
    val transmission: String,
    val seats: Int,
    val features: List<String>
)

data class Flight(
    var id: Int = 0,
    var flightNumber: String,
    var company: String,
    var departureCity: String,
    var arrivalCity: String,
    var departureDate: Date,
    var arrivalDate: Date,
    var terminal: Int,
    var gate: Int,
    var type: String,
    var price: Double
)

data class Hotel(
    val id: Int = 0,
    val name: String,
    val location: String,
    val rating: Float,
    val imageUrl: String,
    val amenities: List<String>,
    val description: String,
    val price: Double
)

data class Place(
    val id: Int = 0,
    val name: String,
    val location: String,
    val type: String,
    val rating: Float,
    val imageUrl: String,
    val openHour: Date,
    val closeHour: Date,
    val price: Double,
)

data class Preferences(
    val language: LanguageOption,
    val currency: CurrencyOption,
    val dateFormat: DateFormatOption,
    val theme: ThemeOption,
    val textSize: TextSizeOption,
    val tripRemindersEnabled: Boolean,
    val weeklySummaryEnabled: Boolean,
    val termsAccepted: Boolean?,
    val username: String = "",
    val dateOfBirth: String = ""
)

data class Trip(
    var id: Int = 0, //ide del trip
    var title: String,
    var startDate: String, // format: dd/MM/yyyy
    var endDate: String,   // format: dd/MM/yyyy
    var price: Double,
    var hotel: HashMap<Int, Int>, //num dia / id hotel
    var flight: HashMap<Int, Int>,
    var car: HashMap<Int, Int>,
    var places: HashMap<Int, MutableList<Int>>,
    var gallery: LinkedList<String>,
    var description: String
)

object ActivityTable : BaseDbTable<Activity>(
    tableName = "activities",
    createSql = """
        CREATE TABLE IF NOT EXISTS activities (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            trip_id INTEGER NOT NULL,
            title TEXT NOT NULL,
            description TEXT NOT NULL,
            date TEXT NOT NULL,
            time TEXT NOT NULL,
            price REAL NOT NULL DEFAULT 0
        )
    """.trimIndent()
) {
    override fun toContentValues(item: Activity): ContentValues = ContentValues().apply {
        put("trip_id", item.tripId)
        put("title", item.title)
        put("description", item.description)
        put("date", item.date.toString())
        put("time", item.time.toString())
        put("price", item.price)
    }

    override fun fromCursor(cursor: Cursor): Activity = Activity(
        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
        tripId = cursor.getInt(cursor.getColumnIndexOrThrow("trip_id")),
        title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
        date = LocalDate.parse(cursor.getString(cursor.getColumnIndexOrThrow("date"))),
        time = LocalTime.parse(cursor.getString(cursor.getColumnIndexOrThrow("time"))),
        price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
    )
}

object CarTable : BaseDbTable<Car>(
    tableName = "cars",
    createSql = """
        CREATE TABLE IF NOT EXISTS cars (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            model TEXT NOT NULL,
            type TEXT NOT NULL,
            price_per_day REAL NOT NULL,
            image_url TEXT NOT NULL,
            transmission TEXT NOT NULL,
            seats INTEGER NOT NULL,
            features TEXT NOT NULL
        )
    """.trimIndent()
) {
    override fun toContentValues(item: Car): ContentValues = ContentValues().apply {
        put("model", item.model)
        put("type", item.type)
        put("price_per_day", item.pricePerDay)
        put("image_url", item.imageUrl)
        put("transmission", item.transmission)
        put("seats", item.seats)
        put("features", DbCodec.encodeStringList(item.features))
    }

    override fun fromCursor(cursor: Cursor): Car = Car(
        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
        model = cursor.getString(cursor.getColumnIndexOrThrow("model")),
        type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
        pricePerDay = cursor.getDouble(cursor.getColumnIndexOrThrow("price_per_day")),
        imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
        transmission = cursor.getString(cursor.getColumnIndexOrThrow("transmission")),
        seats = cursor.getInt(cursor.getColumnIndexOrThrow("seats")),
        features = DbCodec.decodeStringList(cursor.getString(cursor.getColumnIndexOrThrow("features")))
    )
}

object FlightTable : BaseDbTable<Flight>(
    tableName = "flights",
    createSql = """
        CREATE TABLE IF NOT EXISTS flights (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            flight_number TEXT NOT NULL,
            company TEXT NOT NULL,
            departure_city TEXT NOT NULL,
            arrival_city TEXT NOT NULL,
            departure_date INTEGER NOT NULL,
            arrival_date INTEGER NOT NULL,
            terminal INTEGER NOT NULL,
            gate INTEGER NOT NULL,
            type TEXT NOT NULL,
            price REAL NOT NULL
        )
    """.trimIndent()
) {
    override fun toContentValues(item: Flight): ContentValues = ContentValues().apply {
        put("flight_number", item.flightNumber)
        put("company", item.company)
        put("departure_city", item.departureCity)
        put("arrival_city", item.arrivalCity)
        put("departure_date", item.departureDate.time)
        put("arrival_date", item.arrivalDate.time)
        put("terminal", item.terminal)
        put("gate", item.gate)
        put("type", item.type)
        put("price", item.price)
    }

    override fun fromCursor(cursor: Cursor): Flight = Flight(
        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
        flightNumber = cursor.getString(cursor.getColumnIndexOrThrow("flight_number")),
        company = cursor.getString(cursor.getColumnIndexOrThrow("company")),
        departureCity = cursor.getString(cursor.getColumnIndexOrThrow("departure_city")),
        arrivalCity = cursor.getString(cursor.getColumnIndexOrThrow("arrival_city")),
        departureDate = Date(cursor.getLong(cursor.getColumnIndexOrThrow("departure_date"))),
        arrivalDate = Date(cursor.getLong(cursor.getColumnIndexOrThrow("arrival_date"))),
        terminal = cursor.getInt(cursor.getColumnIndexOrThrow("terminal")),
        gate = cursor.getInt(cursor.getColumnIndexOrThrow("gate")),
        type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
        price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
    )
}

object HotelTable : BaseDbTable<Hotel>(
    tableName = "hotels",
    createSql = """
        CREATE TABLE IF NOT EXISTS hotels (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            location TEXT NOT NULL,
            rating REAL NOT NULL,
            image_url TEXT NOT NULL,
            amenities TEXT NOT NULL,
            description TEXT NOT NULL,
            price REAL NOT NULL
        )
    """.trimIndent()
) {
    override fun toContentValues(item: Hotel): ContentValues = ContentValues().apply {
        put("name", item.name)
        put("location", item.location)
        put("rating", item.rating)
        put("image_url", item.imageUrl)
        put("amenities", DbCodec.encodeStringList(item.amenities))
        put("description", item.description)
        put("price", item.price)
    }

    override fun fromCursor(cursor: Cursor): Hotel = Hotel(
        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
        location = cursor.getString(cursor.getColumnIndexOrThrow("location")),
        rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating")),
        imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
        amenities = DbCodec.decodeStringList(cursor.getString(cursor.getColumnIndexOrThrow("amenities"))),
        description = cursor.getString(cursor.getColumnIndexOrThrow("description")),
        price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
    )
}

object PlaceTable : BaseDbTable<Place>(
    tableName = "places",
    createSql = """
        CREATE TABLE IF NOT EXISTS places (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            location TEXT NOT NULL,
            type TEXT NOT NULL,
            rating REAL NOT NULL,
            image_url TEXT NOT NULL,
            open_hour INTEGER NOT NULL,
            close_hour INTEGER NOT NULL,
            price REAL NOT NULL
        )
    """.trimIndent()
) {
    override fun toContentValues(item: Place): ContentValues = ContentValues().apply {
        put("name", item.name)
        put("location", item.location)
        put("type", item.type)
        put("rating", item.rating)
        put("image_url", item.imageUrl)
        put("open_hour", item.openHour.time)
        put("close_hour", item.closeHour.time)
        put("price", item.price)
    }

    override fun fromCursor(cursor: Cursor): Place = Place(
        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
        name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
        location = cursor.getString(cursor.getColumnIndexOrThrow("location")),
        type = cursor.getString(cursor.getColumnIndexOrThrow("type")),
        rating = cursor.getFloat(cursor.getColumnIndexOrThrow("rating")),
        imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("image_url")),
        openHour = Date(cursor.getLong(cursor.getColumnIndexOrThrow("open_hour"))),
        closeHour = Date(cursor.getLong(cursor.getColumnIndexOrThrow("close_hour"))),
        price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
    )
}

object PreferencesTable : BaseDbTable<Preferences>(
    tableName = "preferences",
    createSql = """
        CREATE TABLE IF NOT EXISTS preferences (
            id INTEGER PRIMARY KEY CHECK(id = 1),
            language TEXT NOT NULL,
            currency TEXT NOT NULL,
            date_format TEXT NOT NULL,
            theme TEXT NOT NULL,
            text_size TEXT NOT NULL,
            trip_reminders_enabled INTEGER NOT NULL,
            weekly_summary_enabled INTEGER NOT NULL,
            terms_accepted INTEGER,
            username TEXT NOT NULL,
            date_of_birth TEXT NOT NULL
        )
    """.trimIndent()
) {
    override fun toContentValues(item: Preferences): ContentValues = ContentValues().apply {
        put("id", 1)
        put("language", item.language.name)
        put("currency", item.currency.name)
        put("date_format", item.dateFormat.name)
        put("theme", item.theme.name)
        put("text_size", item.textSize.name)
        put("trip_reminders_enabled", if (item.tripRemindersEnabled) 1 else 0)
        put("weekly_summary_enabled", if (item.weeklySummaryEnabled) 1 else 0)
        put("terms_accepted", item.termsAccepted?.let { if (it) 1 else 0 })
        put("username", item.username)
        put("date_of_birth", item.dateOfBirth)
    }

    override fun fromCursor(cursor: Cursor): Preferences = Preferences(
        language = runCatching {
            LanguageOption.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("language")))
        }.getOrDefault(LanguageOption.ENGLISH),
        currency = runCatching {
            CurrencyOption.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("currency")))
        }.getOrDefault(CurrencyOption.EUR),
        dateFormat = runCatching {
            DateFormatOption.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("date_format")))
        }.getOrDefault(DateFormatOption.DD_MM_YYYY),
        theme = runCatching {
            ThemeOption.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("theme")))
        }.getOrDefault(ThemeOption.LIGHT),
        textSize = runCatching {
            TextSizeOption.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("text_size")))
        }.getOrDefault(TextSizeOption.NORMAL),
        tripRemindersEnabled = cursor.getInt(cursor.getColumnIndexOrThrow("trip_reminders_enabled")) == 1,
        weeklySummaryEnabled = cursor.getInt(cursor.getColumnIndexOrThrow("weekly_summary_enabled")) == 1,
        termsAccepted = if (cursor.isNull(cursor.getColumnIndexOrThrow("terms_accepted"))) {
            null
        } else {
            cursor.getInt(cursor.getColumnIndexOrThrow("terms_accepted")) == 1
        },
        username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
        dateOfBirth = cursor.getString(cursor.getColumnIndexOrThrow("date_of_birth"))
    )
}

object TripTable : BaseDbTable<Trip>(
    tableName = "trips",
    createSql = """
        CREATE TABLE IF NOT EXISTS trips (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            title TEXT NOT NULL,
            start_date TEXT NOT NULL,
            end_date TEXT NOT NULL,
            price REAL NOT NULL,
            hotel TEXT NOT NULL,
            flight TEXT NOT NULL,
            car TEXT NOT NULL,
            places TEXT NOT NULL,
            gallery TEXT NOT NULL,
            description TEXT NOT NULL
        )
    """.trimIndent()
) {
    override fun toContentValues(item: Trip): ContentValues = ContentValues().apply {
        put("title", item.title)
        put("start_date", item.startDate)
        put("end_date", item.endDate)
        put("price", item.price)
        put("hotel", DbCodec.encodeIntMap(item.hotel))
        put("flight", DbCodec.encodeIntMap(item.flight))
        put("car", DbCodec.encodeIntMap(item.car))
        put("places", DbCodec.encodeIntListMap(item.places))
        put("gallery", DbCodec.encodeStringList(item.gallery.toList()))
        put("description", item.description)
    }

    override fun fromCursor(cursor: Cursor): Trip = Trip(
        id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
        title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
        startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date")),
        endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date")),
        price = cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
        hotel = DbCodec.decodeIntMap(cursor.getString(cursor.getColumnIndexOrThrow("hotel"))),
        flight = DbCodec.decodeIntMap(cursor.getString(cursor.getColumnIndexOrThrow("flight"))),
        car = DbCodec.decodeIntMap(cursor.getString(cursor.getColumnIndexOrThrow("car"))),
        places = DbCodec.decodeIntListMap(cursor.getString(cursor.getColumnIndexOrThrow("places"))),
        gallery = LinkedList(DbCodec.decodeStringList(cursor.getString(cursor.getColumnIndexOrThrow("gallery")))),
        description = cursor.getString(cursor.getColumnIndexOrThrow("description"))
    )
}

val ALL_DB_TABLES: List<DbTable<*>> = listOf(
    ActivityTable,
    CarTable,
    FlightTable,
    HotelTable,
    PlaceTable,
    PreferencesTable,
    TripTable
)

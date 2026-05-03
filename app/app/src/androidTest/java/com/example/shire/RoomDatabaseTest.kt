package com.example.shire

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shire.db.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
class RoomDatabaseTest {
    private lateinit var userDao: UserDao
    private lateinit var tripDao: TripDao
    private lateinit var activityDao: ActivityDao
    private lateinit var db: ShireRoomDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ShireRoomDatabase::class.java).build()
        userDao = db.userDao()
        tripDao = db.tripDao()
        activityDao = db.activityDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() = runTest {
        val user = User(
            name = "Test User",
            email = "test@example.com",
            passwordHash = "hash",
            username = "testuser"
        )
        val userId = userDao.upsert(user).toInt()
        val byUsername = userDao.getByUsername("testuser").first()
        assertNotNull(byUsername)
        assertEquals(userId, byUsername?.id)
        assertEquals("test@example.com", byUsername?.email)
    }

    @Test
    @Throws(Exception::class)
    fun writeTripAndReadInList() = runTest {
        // Create user first for FK constraint
        val userId = userDao.upsert(User(name = "User", email = "u@e.c", passwordHash = "h", username = "u")).toInt()

        val trip = Trip(
            userId = userId,
            title = "Paris Trip",
            startDate = "01/01/2026",
            endDate = "10/01/2026",
            price = 1000.0,
            hotel = hashMapOf(),
            flight = hashMapOf(),
            car = hashMapOf(),
            places = hashMapOf(),
            gallery = java.util.LinkedList(),
            description = "Desc"
        )
        val tripId = tripDao.insert(trip).toInt()
        val userTrips = tripDao.getByUserId(userId).first()
        assertEquals(1, userTrips.size)
        assertEquals("Paris Trip", userTrips[0].title)
        assertEquals(tripId, userTrips[0].id)
    }

    @Test
    @Throws(Exception::class)
    fun writeActivityAndReadByTrip() = runTest {
        val userId = userDao.upsert(User(name = "U", email = "u@e.c", passwordHash = "h", username = "u")).toInt()
        val tripId = tripDao.insert(Trip(userId = userId, title = "T", startDate = "S", endDate = "E", price = 0.0, hotel = hashMapOf(), flight = hashMapOf(), car = hashMapOf(), places = hashMapOf(), gallery = java.util.LinkedList(), description = "D")).toInt()

        val activity = Activity(
            tripId = tripId,
            title = "Louvre",
            description = "Museum",
            date = LocalDate.now(),
            time = LocalTime.now(),
            price = 20.0
        )
        activityDao.insert(activity)
        val activities = activityDao.getByTripId(tripId).first()
        assertEquals(1, activities.size)
        assertEquals("Louvre", activities[0].title)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTripCascadesToActivities() = runTest {
        val userId = userDao.upsert(User(name = "U", email = "u@e.c", passwordHash = "h", username = "u")).toInt()
        val tripId = tripDao.insert(Trip(userId = userId, title = "T", startDate = "S", endDate = "E", price = 0.0, hotel = hashMapOf(), flight = hashMapOf(), car = hashMapOf(), places = hashMapOf(), gallery = java.util.LinkedList(), description = "D")).toInt()

        activityDao.insert(Activity(tripId = tripId, title = "A1", description = "D1", date = LocalDate.now(), time = LocalTime.now()))
        
        val initialActivities = activityDao.getByTripId(tripId).first()
        assertEquals(1, initialActivities.size)

        tripDao.deleteByUserIdAndTripId(userId, tripId)
        
        // Wait for cascade
        val postDeleteActivities = activityDao.getByTripId(tripId).first()
        assertEquals(0, postDeleteActivities.size)
    }
}

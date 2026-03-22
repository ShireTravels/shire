package com.example.shire.domain.repository

import com.example.shire.domain.model.Activity
import java.time.LocalDate
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test

class ActivityRepositoryTest {

    private lateinit var repository: ActivityRepositoryImpl

    @Before
    fun setUp() {
        repository = ActivityRepositoryImpl()
    }

    @Test
    fun addActivity_increasesCountAndReturnsValidId() {
        val initialSize = repository.getActivitiesForTrip(1).size
        val newActivity = Activity(
            id = 0,
            tripId = 1,
            title = "Test Activity",
            description = "Test Desc",
            date = LocalDate.of(2026, 6, 1),
            time = LocalTime.of(12, 0)
        )
        val added = repository.addActivity(newActivity)
        
        assertNotNull(added)
        assertTrue(added.id > 0)
        assertEquals(initialSize + 1, repository.getActivitiesForTrip(1).size)
    }

    @Test
    fun getActivity_returnsCorrectActivity() {
        val added = repository.addActivity(Activity(0, 1, "Title", "Desc", LocalDate.now(), LocalTime.now(), 0.0))
        val retrieved = repository.getActivity(added.id)
        assertNotNull(retrieved)
        assertEquals("Title", retrieved?.title)
    }

    @Test
    fun updateActivity_modifiesExistingData() {
        val added = repository.addActivity(Activity(0, 1, "Old Title", "Desc", LocalDate.now(), LocalTime.now(), 0.0))
        val updatedActivity = added.copy(title = "New Title")
        val success = repository.updateActivity(updatedActivity)
        
        assertTrue(success)
        assertEquals("New Title", repository.getActivity(added.id)?.title)
    }

    @Test
    fun deleteActivity_removesActivity() {
        val added = repository.addActivity(Activity(0, 1, "Title", "Desc", LocalDate.now(), LocalTime.now()))
        val success = repository.deleteActivity(added.id)
        
        assertTrue(success)
        assertNull(repository.getActivity(added.id))
    }
}

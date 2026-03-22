package com.example.shire.domain.repository

import com.example.shire.domain.model.Place
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.Date

class PlaceRepositoryTest {
    private lateinit var placeRepository: PlaceRepositoryImpl

    @Before
    fun setUp() {
        placeRepository = PlaceRepositoryImpl()
    }

    @Test
    fun testGetPlaces_returnsInitialList() {
        val places = placeRepository.getPlaces()
        assertTrue(places.isNotEmpty())
    }

    @Test
    fun testGetPlace_existingId_returnsPlace() {
        val place = placeRepository.getPlace(401)
        assertNotNull(place)
        assertEquals("Louvre Museum", place.name)
    }

    @Test
    fun testAddPlace_addsToList() {
        val initialSize = placeRepository.getPlaces().size
        val newPlace = Place(id = 999, name = "Test Place", location = "Test", type = "Museum", rating = 4.0f, imageUrl = "", openHour = Date(), closeHour = Date(), price = 0.0)
        
        placeRepository.addPlace(newPlace)
        assertEquals(initialSize + 1, placeRepository.getPlaces().size)
    }

    @Test
    fun testDeletePlace_removesFromList() {
        val initialSize = placeRepository.getPlaces().size
        val success = placeRepository.deletePlace(401)
        
        assertTrue(success)
        assertEquals(initialSize - 1, placeRepository.getPlaces().size)
    }

    @Test
    fun testUpdatePlace_modifiesExistingPlace() {
        val existing = placeRepository.getPlace(401)
        val updated = existing.copy(name = "Updated Louvre")
        
        val success = placeRepository.updatePlace(updated)
        assertTrue(success)
        assertEquals("Updated Louvre", placeRepository.getPlace(401).name)
    }
}

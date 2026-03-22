package com.example.shire.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.shire.domain.model.Activity
import com.example.shire.domain.model.Trip
import com.example.shire.domain.repository.ActivityRepository
import com.example.shire.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import android.util.Log

@HiltViewModel
class ItineraryViewModel @Inject constructor(
    private val activityRepository: ActivityRepository,
    private val tripRepository: TripRepository
) : ViewModel() {

    var errorMessage by mutableStateOf<String?>(null)

    fun getActivitiesForTrip(tripId: Int): List<Activity> {
        Log.d("ItineraryVM", "Fetching activities for trip $tripId")
        return activityRepository.getActivitiesForTrip(tripId).sortedBy { it.time }
    }

    fun addActivity(
        tripId: Int,
        title: String,
        description: String,
        date: LocalDate?,
        time: LocalTime?,
        price: Double = 0.0
    ): Boolean {
        if (!validateActivityInput(tripId, title, description, date, time)) return false

        val newActivity = Activity(
            tripId = tripId,
            title = title,
            description = description,
            date = date!!,
            time = time!!,
            price = price
        )
        activityRepository.addActivity(newActivity)
        Log.i("ItineraryVM", "Added new activity successfully: $title")
        return true
    }

    fun updateActivity(
        activityId: Int,
        tripId: Int,
        title: String,
        description: String,
        date: LocalDate?,
        time: LocalTime?,
        price: Double = 0.0
    ): Boolean {
        if (!validateActivityInput(tripId, title, description, date, time)) return false

        val updatedActivity = Activity(
            id = activityId,
            tripId = tripId,
            title = title,
            description = description,
            date = date!!,
            time = time!!,
            price = price
        )
        val success = activityRepository.updateActivity(updatedActivity)
        if (success) Log.i("ItineraryVM", "Updated activity successfully: $title")
        else Log.e("ItineraryVM", "Failed to update activity: $title")
        return success
    }

    fun deleteActivity(activityId: Int): Boolean {
        val success = activityRepository.deleteActivity(activityId)
        if (success) Log.i("ItineraryVM", "Deleted activity $activityId")
        else Log.e("ItineraryVM", "Failed to delete activity $activityId")
        return success
    }

    private fun validateActivityInput(
        tripId: Int,
        title: String,
        description: String,
        date: LocalDate?,
        time: LocalTime?
    ): Boolean {
        if (title.isBlank()) {
            errorMessage = "El título no puede estar vacío."
            Log.w("ItineraryVM", "Validation failed: Title is empty")
            return false
        }
        if (date == null) {
            errorMessage = "Debe seleccionar una fecha válida."
            Log.w("ItineraryVM", "Validation failed: Date is null")
            return false
        }
        if (time == null) {
            errorMessage = "Debe seleccionar una hora válida."
            Log.w("ItineraryVM", "Validation failed: Time is null")
            return false
        }

        val trip = tripRepository.getTrip(tripId)
        if (trip != null) {
            try {
                val formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
                val tripStartDate = LocalDate.parse(trip.startDate, formatter)
                val tripEndDate = LocalDate.parse(trip.endDate, formatter)

                if (date.isBefore(tripStartDate) || date.isAfter(tripEndDate)) {
                    errorMessage = "La actividad debe estar dentro del rango de fechas del viaje (${trip.startDate} - ${trip.endDate})."
                    Log.w("ItineraryVM", "Validation failed: Activity date $date outside trip range")
                    return false
                }
            } catch (e: Exception) {
                Log.e("ItineraryVM", "Error parsing trip dates for validation", e)
                // If there's an error parsing the formats, better to just let it pass loosely or log it.
                // In a robust implementation, the models would store Date or LocalDate instances.
            }
        }
        
        errorMessage = null
        return true
    }
}

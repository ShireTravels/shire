package com.example.shire.domain.repository

import androidx.compose.runtime.mutableStateListOf
import com.example.shire.domain.model.Activity
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

@Singleton
class ActivityRepositoryImpl @Inject constructor() : ActivityRepository {
    private val activityList = mutableStateListOf<Activity>()

    init {
        // Add some mock data to test initially
        activityList.addAll(
            listOf(
                Activity(
                    id = 1,
                    tripId = 1,
                    title = "Visita al Louvre",
                    description = "Entrada reservada para las 10 AM",
                    date = LocalDate.of(2026, 4, 13),
                    time = LocalTime.of(10, 0)
                ),
                Activity(
                    id = 2,
                    tripId = 1,
                    title = "Cena en la Torre Eiffel",
                    description = "Cena romántica en el restaurante de la torre",
                    date = LocalDate.of(2026, 4, 15),
                    time = LocalTime.of(21, 0)
                ),
                Activity(
                    id = 3,
                    tripId = 2,
                    title = "Tour por el Coliseo",
                    description = "Visita guiada al Coliseo y Foro Romano",
                    date = LocalDate.of(2026, 5, 6),
                    time = LocalTime.of(9, 30)
                )
            )
        )
    }

    override fun getActivity(activityId: Int): Activity? {
        Log.d("ActivityRepo", "Fetching activity with id: $activityId")
        return activityList.find { it.id == activityId }
    }

    override fun getActivitiesForTrip(tripId: Int): List<Activity> {
        Log.d("ActivityRepo", "Fetching activities for trip: $tripId")
        return activityList.filter { it.tripId == tripId }
    }

    override fun addActivity(activity: Activity): Activity {
        val nextId = (activityList.maxOfOrNull { it.id } ?: 0) + 1
        val newActivity = activity.copy(id = nextId)
        activityList.add(newActivity)
        Log.i("ActivityRepo", "Added new activity: ${newActivity.title} (ID: ${newActivity.id})")
        return newActivity
    }

    override fun updateActivity(activity: Activity): Boolean {
        val index = activityList.indexOfFirst { it.id == activity.id }
        if (index != -1) {
            activityList[index] = activity
            Log.i("ActivityRepo", "Updated activity successfully (ID: ${activity.id})")
            return true
        }
        Log.e("ActivityRepo", "Failed to update activity: ID ${activity.id} not found")
        return false
    }

    override fun deleteActivity(activityId: Int): Boolean {
        val index = activityList.indexOfFirst { it.id == activityId }
        if (index != -1) {
            activityList.removeAt(index)
            Log.i("ActivityRepo", "Deleted activity successfully (ID: $activityId)")
            return true
        }
        Log.e("ActivityRepo", "Failed to delete activity: ID $activityId not found")
        return false
    }
}

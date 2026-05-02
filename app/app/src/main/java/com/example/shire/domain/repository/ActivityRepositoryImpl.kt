package com.example.shire.domain.repository

import android.content.Context
import android.util.Log
import com.example.shire.db.Activity as DbActivity
import com.example.shire.db.db
import com.example.shire.domain.model.Activity
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivityRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : ActivityRepository {

    private val database = db(context)

    override fun getActivity(activityId: Int): Activity? {
        Log.d("ActivityRepo", "Fetching activity with id: $activityId")
        return database.getActivityById(activityId)?.toDomainActivity()
    }

    override fun getActivitiesForTrip(tripId: Int): List<Activity> {
        Log.d("ActivityRepo", "Fetching activities for trip: $tripId")
        return database.getActivitiesByTrip(tripId).map { it.toDomainActivity() }
    }

    override fun addActivity(activity: Activity): Activity {
        val insertedId = database.insertActivity(activity.toDbActivity()).toInt()
        val persistedActivity = if (activity.id > 0) activity else activity.copy(id = insertedId)
        Log.i("ActivityRepo", "Added new activity: ${persistedActivity.title} (ID: ${persistedActivity.id})")
        return persistedActivity
    }

    override fun updateActivity(activity: Activity): Boolean {
        val exists = database.getActivityById(activity.id) != null
        if (exists) {
            database.insertActivity(activity.toDbActivity())
            Log.i("ActivityRepo", "Updated activity successfully (ID: ${activity.id})")
            return true
        }
        Log.e("ActivityRepo", "Failed to update activity: ID ${activity.id} not found")
        return false
    }

    override fun deleteActivity(activityId: Int): Boolean {
        val deleted = database.deleteActivity(activityId) > 0
        if (deleted) {
            Log.i("ActivityRepo", "Deleted activity successfully (ID: $activityId)")
            return true
        }
        Log.e("ActivityRepo", "Failed to delete activity: ID $activityId not found")
        return false
    }

    private fun Activity.toDbActivity(): DbActivity = DbActivity(
        id = id,
        tripId = tripId,
        title = title,
        description = description,
        date = date,
        time = time,
        price = price
    )

    private fun DbActivity.toDomainActivity(): Activity = Activity(
        id = id,
        tripId = tripId,
        title = title,
        description = description,
        date = date,
        time = time,
        price = price
    )
}

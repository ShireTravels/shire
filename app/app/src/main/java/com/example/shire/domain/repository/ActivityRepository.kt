package com.example.shire.domain.repository

import com.example.shire.domain.model.Activity
import kotlinx.coroutines.flow.Flow

interface ActivityRepository {
    fun getActivity(activityId: Int): Activity?
    fun getActivitiesForTrip(tripId: Int): Flow<List<Activity>>
    fun addActivity(activity: Activity): Activity
    fun updateActivity(activity: Activity): Boolean
    fun deleteActivity(activityId: Int): Boolean
}

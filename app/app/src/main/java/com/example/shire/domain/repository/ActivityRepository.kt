package com.example.shire.domain.repository

import com.example.shire.domain.model.Activity

interface ActivityRepository {
    fun getActivity(activityId: Int): Activity?
    fun getActivitiesForTrip(tripId: Int): List<Activity>
    fun addActivity(activity: Activity): Activity
    fun updateActivity(activity: Activity): Boolean
    fun deleteActivity(activityId: Int): Boolean
}

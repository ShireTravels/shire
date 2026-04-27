package com.example.shire

import android.app.Application
import com.example.shire.db.db
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
	override fun onCreate() {
		super.onCreate()
		db.initialize(this)
	}
}
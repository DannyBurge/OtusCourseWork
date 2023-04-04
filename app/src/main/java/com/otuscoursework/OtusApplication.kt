package com.otuscoursework

import android.app.Application
import com.github.ajalt.timberkt.Timber
import com.otuscoursework.ui.main.MainActivity
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OtusApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        INSTANCE = this
    }



    companion object {
        lateinit var INSTANCE: OtusApplication
    }
}
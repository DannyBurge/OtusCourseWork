package com.otuscoursework

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp
class OtusApplication : Application() {

    override fun onCreate() {
        Timber.plant(DebugTree())
        super.onCreate()
        INSTANCE = this
    }


    companion object {
        lateinit var INSTANCE: OtusApplication
    }
}
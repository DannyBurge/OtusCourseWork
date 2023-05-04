package com.otuscoursework

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OtusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    companion object {
        lateinit var INSTANCE: OtusApplication
    }
}
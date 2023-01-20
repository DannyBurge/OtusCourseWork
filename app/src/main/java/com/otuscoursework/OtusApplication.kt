package com.otuscoursework

import android.app.Application
import com.github.ajalt.timberkt.Timber

class OtusApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
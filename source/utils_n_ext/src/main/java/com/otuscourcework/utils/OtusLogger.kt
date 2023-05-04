package com.otuscourcework.utils

import timber.log.Timber

class OtusLogger {

    fun init() {
        Timber.plant(Timber.DebugTree())
    }

    fun log(t: Throwable) {
        Timber.tag(DEBUG_TAG).d(t)
    }

    fun log(msg: Any) {
        Timber.tag(DEBUG_TAG).d(msg.toString())
    }

    companion object {
        const val DEBUG_TAG = "OtusDebugTag"
    }
}
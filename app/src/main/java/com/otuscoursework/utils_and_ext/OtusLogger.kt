package com.otuscoursework.utils_and_ext

import timber.log.Timber

object OtusLogger {
    fun log(t: Throwable) {
        Timber.tag(DEBUG_TAG).d(t)
    }
}
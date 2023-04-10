package com.otuscoursework.resource

import android.content.Context
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import javax.inject.Inject

class ResHelper @Inject constructor(private val context: Context) {
    fun getStringById(@StringRes id: Int, vararg formatArgs: Any): String {
        return context.getString(id, formatArgs)
    }

    fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return context.resources.getDimensionPixelSize(id)
    }

    fun getDimension(@DimenRes id: Int): Int {
        return context.resources.getDimensionPixelOffset(id)
    }
}
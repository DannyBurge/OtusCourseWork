package com.otuscoursework.resource

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import javax.inject.Inject

class ResHelper @Inject constructor(private val context: Context) {

    private val res = context.resources
    fun getStringById(@StringRes id: Int, vararg formatArgs: Any): String {
        return res.getString(id, formatArgs)
    }

    fun getDimensionPixelSize(@DimenRes id: Int): Int {
        return res.getDimensionPixelSize(id)
    }

    fun getDimension(@DimenRes id: Int): Int {
        return context.resources.getDimensionPixelOffset(id)
    }

    fun getColor(@ColorRes i: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            res.getColor(i, null)
        } else {
            res.getColor(i)
        }
    }

    fun getDrawable(@DrawableRes id: Int): Drawable {
        return res.getDrawable(id)
    }
}
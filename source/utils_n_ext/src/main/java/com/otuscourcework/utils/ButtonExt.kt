package com.otuscourcework.utils

import android.view.View

fun View.enable(isEnabled: Boolean) {
    isClickable = isEnabled
    isFocusable = isEnabled
    alpha = if (isEnabled) {
        UN_TRANSPARENT
    } else {
        HALF_TRANSPARENT
    }
}

private const val HALF_TRANSPARENT = 0.5F
private const val UN_TRANSPARENT = 1F
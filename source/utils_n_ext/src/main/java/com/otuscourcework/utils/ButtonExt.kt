package com.otuscourcework.utils

import android.view.View
import android.widget.Button

fun View.enable(isEnabled: Boolean) {
    isClickable = isEnabled
    isFocusable = isEnabled
    alpha = if (isEnabled) {
        1.0F
    } else {
        0.5F
    }
}
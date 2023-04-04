package com.otuscoursework.utils_and_ext

import android.widget.Button

fun Button.enable(isEnabled: Boolean) {
    isClickable = isEnabled
    isFocusable = isEnabled
    alpha = if (isEnabled) {
        1.0F
    } else {
        0.6F
    }
}
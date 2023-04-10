package com.otuscourcework.utils

import java.text.SimpleDateFormat
import java.util.*

private const val FORMAT_FULL_ISO_DATE = "dd.MM.yyyy HH:mm"

fun Date.toFullIsoDate(): String = format(FORMAT_FULL_ISO_DATE, this)

private fun format(pattern: String, date: Date) =
    SimpleDateFormat(pattern, Locale.forLanguageTag("ru")).format(date)
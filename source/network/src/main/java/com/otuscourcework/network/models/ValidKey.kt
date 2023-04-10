package com.otuscourcework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidKey(
    @Json(name = "key") val key: String,
    @Json(name = "valid") val isValid: Boolean
)
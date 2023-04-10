package com.otuscourcework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDeliveryAddress(
    @Json(name = "id") val id: Int?,
    @Json(name = "name") val name: String,
    @Json(name = "address") val address: String
)
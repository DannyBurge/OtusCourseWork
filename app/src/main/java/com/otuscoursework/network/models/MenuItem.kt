package com.otuscoursework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenuItem(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "price") val price: Int,
    @Json(name = "description") val description: String,
    @Json(name = "sizes") val sizes: List<MenuItemSize>,
)

@JsonClass(generateAdapter = true)
data class MenuItemSize(
    @Json(name = "price") val price: Int,
    @Json(name = "display_name") val displayName: String,
)
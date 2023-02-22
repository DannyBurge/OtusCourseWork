package com.otuscoursework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenuItem(
    @Json(name = "id") val id: Int,
    @Json(name = "category_id") val categoryId: Int,
    @Json(name = "picture") val picture: String,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "sizes") val sizes: List<MenuItemSize>,
)

@JsonClass(generateAdapter = true)
data class MenuItemSize(
    @Json(name = "size_id") val sizeId: Int,
    @Json(name = "price") val price: Int,
    @Json(name = "display_name") val displayName: String,
)
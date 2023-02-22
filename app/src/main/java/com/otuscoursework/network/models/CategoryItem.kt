package com.otuscoursework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryItem(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)
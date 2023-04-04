package com.otuscoursework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ValidateCodeItem(
    @Json(name = "id") val id: Int?,
    @Json(name = "code") val code: String,
    @Json(name = "validated") val validated: Boolean,
    @Json(name = "amount_added") val amountAdded: Int,
)
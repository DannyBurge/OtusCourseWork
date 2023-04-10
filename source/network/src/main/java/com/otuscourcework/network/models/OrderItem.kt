package com.otuscourcework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderItem(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "price") val price: Int,
    @Json(name = "tokens_amount") val tokensAmount: Int,
    @Json(name = "address") val address: String,
    @Json(name = "date") val date: String,
    @Json(name = "active") val isActive: Boolean,
    @Json(name = "positions") val positions: List<OrderPosition>,
)

@JsonClass(generateAdapter = true)
data class OrderPosition(
    @Json(name = "group_id") val groupId: Int,
    @Json(name = "size_id") val sizeId: Int,
    @Json(name = "count") val amount: Int,
)
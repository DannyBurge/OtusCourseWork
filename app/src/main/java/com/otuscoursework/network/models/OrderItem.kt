package com.otuscoursework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OrderItem(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "price") val price: Int,
    @Json(name = "tokens_amount") val tokensAmount: Int,
    @Json(name = "address") val address: String,
    @Json(name = "positions") val positions: List<OrderPosition>,
)

@JsonClass(generateAdapter = true)
data class OrderPosition(
    @Json(name = "name") val name: String,
    @Json(name = "count") val count: Int,
    @Json(name = "size") val size: MenuItemSize,
)
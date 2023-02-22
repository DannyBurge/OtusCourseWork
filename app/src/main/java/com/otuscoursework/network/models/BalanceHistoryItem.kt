package com.otuscoursework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BalanceHistoryItem(
    @Json(name = "id") val id: Int,
    @Json(name = "order_id") val orderId: Int,
    @Json(name = "date") val date: String,
    @Json(name = "amount_added") val amountAdded: Int,
    @Json(name = "amount") val amount: Int,
)
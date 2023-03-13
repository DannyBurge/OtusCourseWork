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
    @Json(name = "food_value") val foodValue: MenuItemFoodValue,
)

@JsonClass(generateAdapter = true)
data class MenuItemSize(
    @Json(name = "size_id") val sizeId: Int,
    @Json(name = "price") val price: Int,
    @Json(name = "display_name") val displayName: String,
    @Json(name = "food_value") val foodValue: MenuItemFoodValue,
)

@JsonClass(generateAdapter = true)
data class MenuItemFoodValue(
    @Json(name = "weight") val weight: String,
    @Json(name = "ccal") val ccal: String,
    @Json(name = "proteins") val proteins: String,
    @Json(name = "fats") val fats: String,
    @Json(name = "carbohydrates") val carbohydrates: String,
)
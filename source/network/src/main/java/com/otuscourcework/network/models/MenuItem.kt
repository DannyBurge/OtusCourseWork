package com.otuscourcework.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MenuItem(
    @Json(name = "group_id") val groupId: Int,
    @Json(name = "category_id") val categoryId: Int,
    @Json(name = "picture") val picture: String,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "sub_items") val subItems: List<MenuSubItem>,
    @Json(name = "food_value") val foodValue: MenuItemFoodValue,
)

@JsonClass(generateAdapter = true)
data class MenuSubItem(
    @Json(name = "id") val id: Int,
    @Json(name = "price") val price: Int,
    @Json(name = "display_name") val displayName: String,
    @Json(name = "weight") val weight: Int,
)

@JsonClass(generateAdapter = true)
data class MenuItemFoodValue(
    @Json(name = "ccal") val ccal: String,
    @Json(name = "proteins") val proteins: String,
    @Json(name = "fats") val fats: String,
    @Json(name = "carbohydrates") val carbohydrates: String,
)
package com.otuscoursework.ui.models

import android.os.Parcelable
import com.otuscoursework.network.models.MenuItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItemModel(
    val id: Int,
    val name: String,
    val categoryId: Int,
    val picture: String,
    val description: String,
    var isInFavourite: Boolean,
    val sizes: List<MenuItemSizeUiModel>,
    val amountInCart: MutableList<Int>,
    val foodValue: MenuItemFoodValueModel
) : Parcelable {
    companion object {
        fun from(menuItem: MenuItem): MenuItemModel {
            val sizes = menuItem.sizes.map { size ->

                val foodValue = MenuItemFoodValueModel(
                    weight = size.foodValue.weight,
                    ccal = size.foodValue.ccal,
                    proteins = size.foodValue.proteins,
                    fats = size.foodValue.fats,
                    carbohydrates = size.foodValue.carbohydrates,
                )

                MenuItemSizeUiModel(
                    sizeId = size.sizeId,
                    price = size.price,
                    displayName = size.displayName,
                    foodValue = foodValue
                )
            }
            return MenuItemModel(
                id = menuItem.id,
                name = menuItem.name,
                categoryId = menuItem.categoryId,
                picture = menuItem.picture,
                description = menuItem.description,
                sizes = sizes,
                amountInCart = sizes.map { 0 }.toMutableList(),
                isInFavourite = listOf(
                    true,
                    false
                ).random(), //TODO подгружать список из ПЗУ в HomeFragment(?) и смотреть на него
                foodValue = MenuItemFoodValueModel(
                    weight = menuItem.foodValue.weight,
                    ccal = menuItem.foodValue.ccal,
                    proteins = menuItem.foodValue.proteins,
                    fats = menuItem.foodValue.fats,
                    carbohydrates = menuItem.foodValue.carbohydrates,
                )
            )
        }
    }
}

@Parcelize
data class MenuItemSizeUiModel(
    val sizeId: Int,
    val price: Int,
    val displayName: String,
    val foodValue: MenuItemFoodValueModel
) : Parcelable

@Parcelize
data class MenuItemFoodValueModel(
    val weight: String,
    val ccal: String,
    val proteins: String,
    val fats: String,
    val carbohydrates: String,
) : Parcelable

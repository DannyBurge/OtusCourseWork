package com.otuscoursework.ui.fragments.menuItemDetail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItemDetailModel(
    val groupId: Int,
    val name: String,
    val categoryId: Int,
    val picture: String,
    val description: String,
    var isInFavourite: Boolean,
    val subItems: List<MenuSubItemModel>,
    val amountInCart: MutableList<Int>,
    val foodValue: MenuItemFoodValueModel
) : Parcelable

@Parcelize
data class MenuSubItemModel(
    val id: Int,
    val price: Int,
    val displayName: String,
    val weight: Int,
) : Parcelable

@Parcelize
data class MenuItemFoodValueModel(
    val ccal: String,
    val proteins: String,
    val fats: String,
    val carbohydrates: String,
) : Parcelable
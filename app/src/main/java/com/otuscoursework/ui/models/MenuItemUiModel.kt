package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class MenuItemUiModel(
    override val id: Int,
    val name: String,
    val categoryId: Int,
    val picture: String,
    val description: String,
    var isInFavourite: Boolean,
    val sizes: List<MenuItemSizeUiModel>,
) : RecyclerViewItem

data class MenuItemSizeUiModel(
    val sizeId: Int,
    val price: Int,
    val displayName: String,
)
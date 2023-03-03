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
    var amountInCart: Int = 0,
) : RecyclerViewItem {
    fun toCartItem(itemSizeNum: Int = 1): CartCheckItemUiModel {
        return CartCheckItemUiModel(
            id = this.id,
            name = this.name,
            count = 1,
            price = this.sizes[itemSizeNum].price
        )
    }
}

data class MenuItemSizeUiModel(
    val sizeId: Int,
    val price: Int,
    val displayName: String,
)
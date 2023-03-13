package com.otuscoursework.ui.models

import android.os.Parcelable
import com.otuscoursework.arch.recycler.RecyclerViewItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class MenuItemUiModel(
    override val id: Int,
    val name: String,
    val categoryId: Int,
    val picture: String,
    val description: String,
    var isInFavourite: Boolean,
    val size: MenuItemSizeUiModel,
    var amountInCart: Int = 0,
) : RecyclerViewItem, Parcelable {
    fun toCartItem(): CartCheckItemUiModel {
        return CartCheckItemUiModel(
            id = this.id,
            name = this.name,
            count = 1,
            price = this.size.price
        )
    }
}
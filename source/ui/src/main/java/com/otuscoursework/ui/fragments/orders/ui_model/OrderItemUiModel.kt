package com.otuscoursework.ui.fragments.orders.ui_model

import com.otuscoursework.ui.fragments.cart.ui_model.CartItemUiModel
import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class OrderItemUiModel(
    override val id: Int,
    val isActive: Boolean,
    var isOpen: Boolean,
    val date: String,
    val orderCheckItemList: List<CartItemUiModel>,
    val tokensAdded: Int,
    val totalPrice: Int
) : RecyclerViewItem

package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem
import java.util.*

data class OrderItemUiModel(
    override val id: Int,
    val isActive: Boolean,
    var isOpen: Boolean,
    val date: String,
    val orderCheckItemList: List<CartItemUiModel>,
    val tokensAdded: Int,
    val totalPrice: Int
) : RecyclerViewItem

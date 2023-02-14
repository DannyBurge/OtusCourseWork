package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem
import java.util.*

data class OrderItemUiModel(
    override val id: Int,
    val isActive: Boolean,
    val date: Date,
    val orderCheckItemList: List<CartCheckItemUiModel>,
    val totalPrice: Int
) : RecyclerViewItem

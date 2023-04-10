package com.otuscoursework.ui.fragments.cart.ui_model

import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class CartItemUiModel(
    override val id: Int,
    val name: String,
    val price: Int,
    var amount: Int
) : RecyclerViewItem
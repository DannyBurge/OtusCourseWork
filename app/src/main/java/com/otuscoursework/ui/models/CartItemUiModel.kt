package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class CartItemUiModel(
    override val id: Int,
    val name: String,
    val price: Int,
    var amount: Int
) : RecyclerViewItem
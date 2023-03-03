package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class CartCheckItemUiModel(
    override val id: Int,
    val name: String,
    var count: Int,
    val price: Int
) : RecyclerViewItem {
}

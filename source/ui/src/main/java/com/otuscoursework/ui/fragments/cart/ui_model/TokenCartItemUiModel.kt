package com.otuscoursework.ui.fragments.cart.ui_model

import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class TokenCartItemUiModel(
    override val id: Int,
    val amount: Int
) : RecyclerViewItem

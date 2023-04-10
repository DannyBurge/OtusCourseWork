package com.otuscoursework.ui.fragments.cart.ui_model

import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class AddressItemUiModel(
    override val id: Int,
    val displayName: String,
    val address: String
): RecyclerViewItem

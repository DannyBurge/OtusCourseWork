package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class AddressItemUiModel(
    override val id: Int,
    val displayName: String,
    val address: String
): RecyclerViewItem

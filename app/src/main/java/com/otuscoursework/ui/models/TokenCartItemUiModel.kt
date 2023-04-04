package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class TokenCartItemUiModel(
    override val id: Int,
    val amount: Int
): RecyclerViewItem

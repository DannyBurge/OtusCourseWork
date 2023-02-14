package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class MenuItemUiModel(
    override val id: Int,
    val name: String,
): RecyclerViewItem

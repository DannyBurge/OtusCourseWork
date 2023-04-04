package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class PopupUiItem(
    override val id: Int = 0,
    val name: String
) : RecyclerViewItem

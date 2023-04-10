package com.otuscoursework.ui.views.popup.ui_model

import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class PopupUiItem(
    override val id: Int = 0,
    val name: String
) : RecyclerViewItem

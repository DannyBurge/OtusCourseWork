package com.otuscoursework.ui.views.popup.ui_model

import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class AddPopupUiItem(
    override val id: Int,
    val name: String
) : RecyclerViewItem

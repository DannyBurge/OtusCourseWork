package com.otuscoursework.ui.fragments.home.ui_model

import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class ChipItemUiModel(
    override val id: Int,
    val name: String,
    var isSelected: Boolean
) : RecyclerViewItem

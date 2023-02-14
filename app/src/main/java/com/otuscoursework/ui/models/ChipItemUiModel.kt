package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem

data class ChipItemUiModel(
    override val id: Int,
    val name: String
): RecyclerViewItem

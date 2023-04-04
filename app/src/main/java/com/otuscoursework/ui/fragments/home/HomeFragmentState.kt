package com.otuscoursework.ui.fragments.home

import com.otuscoursework.arch.BaseState
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.ui.models.MenuItemUiModel

data class HomeFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,

    val tokenAmount: Int = 0,
    val chipsItemList: List<ChipItemUiModel> = emptyList(),
    val menuItemList: List<MenuItemUiModel> = emptyList(),
) : BaseState
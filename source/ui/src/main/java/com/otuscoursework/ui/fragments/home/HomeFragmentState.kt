package com.otuscoursework.ui.fragments.home

import com.otuscoursework.ui.arch.BaseState
import com.otuscoursework.ui.fragments.home.ui_model.ChipItemUiModel
import com.otuscoursework.ui.fragments.home.ui_model.MenuItemUiModel

data class HomeFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,

    val tokenAmount: Int = 0,
    val chipsItemList: List<ChipItemUiModel> = emptyList(),
    val menuItemList: List<MenuItemUiModel> = emptyList(),
) : BaseState
package com.otuscoursework.ui.fragments.menuItemDetail

import com.otuscoursework.ui.arch.BaseState

data class MenuItemDetailFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val string: String = "",
) : BaseState
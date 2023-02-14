package com.otuscoursework.ui.fragments.home

import com.otuscoursework.arch.BaseState

data class HomeFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val string: String = "Just started fragment 1",
) : BaseState
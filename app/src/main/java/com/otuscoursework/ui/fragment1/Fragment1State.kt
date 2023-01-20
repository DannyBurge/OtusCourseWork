package com.otuscoursework.ui.fragment1

import com.otuscoursework.arch.BaseState

data class Fragment1State(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val string: String = "Just started fragment 1",
) : BaseState
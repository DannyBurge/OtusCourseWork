package com.otuscoursework.ui.fragment2

import com.otuscoursework.arch.BaseState

data class Fragment2State(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val string: String = "",
) : BaseState
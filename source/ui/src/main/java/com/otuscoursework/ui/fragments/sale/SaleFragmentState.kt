package com.otuscoursework.ui.fragments.sale

import com.otuscoursework.ui.arch.BaseState

data class SaleFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState
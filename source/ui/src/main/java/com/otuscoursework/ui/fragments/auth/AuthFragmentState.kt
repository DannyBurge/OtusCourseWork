package com.otuscoursework.ui.fragments.auth

import com.otuscoursework.ui.arch.BaseState

data class AuthFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null

) : BaseState
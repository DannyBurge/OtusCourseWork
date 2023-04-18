package com.otuscoursework.ui.fragments.settings

import com.otuscoursework.ui.arch.BaseState

data class SettingsFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null
) : BaseState

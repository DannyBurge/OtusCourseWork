package com.otuscoursework.ui.fragments.settings

import com.otuscoursework.ui.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsFragmentViewModel @Inject constructor() : BaseViewModel<SettingsFragmentState>() {
    override var viewModelState = SettingsFragmentState()
}
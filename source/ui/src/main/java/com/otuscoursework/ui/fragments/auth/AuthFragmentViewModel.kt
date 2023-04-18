package com.otuscoursework.ui.fragments.auth

import com.otuscoursework.ui.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthFragmentViewModel @Inject constructor() : BaseViewModel<AuthFragmentState>() {
    override var viewModelState = AuthFragmentState()
}
package com.otuscoursework.ui.arch

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otuscourcework.user_data_keeper.UserDataKeeper
import com.otuscoursework.resource.ResHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel<S> : ViewModel() {

    @Inject
    lateinit var resHelper: ResHelper

    @Inject
    lateinit var userDataKeeper: UserDataKeeper

    abstract var viewModelState: S

    private val _viewModelFlow = MutableStateFlow(viewModelState)
    val viewModelFlow: StateFlow<S> = _viewModelFlow

    protected fun S.render() {
        viewModelState = this
        viewModelScope.launch(Dispatchers.IO) {
            _viewModelFlow.value = viewModelState
        }
    }

    fun getStringById(@StringRes id: Int, vararg formatArgs: Any): String {
        return resHelper.getStringById(id, formatArgs)
    }
}
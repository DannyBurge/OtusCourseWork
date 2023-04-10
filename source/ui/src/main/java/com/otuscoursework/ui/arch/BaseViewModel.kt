package com.otuscoursework.ui.arch

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otuscoursework.resource.ResHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel<S>: ViewModel() {

    @Inject
    lateinit var res: ResHelper

    abstract var viewModelState: S

    private val _viewModelFlow = MutableSharedFlow<S>()
    val viewModelFlow: SharedFlow<S> = _viewModelFlow

    protected fun S.render() {
        viewModelState = this
        viewModelScope.launch(Dispatchers.IO) {
            _viewModelFlow.emit(viewModelState)
        }
    }

    fun getStringById(@StringRes id: Int, vararg formatArgs: Any): String {
        return res.getStringById(id, formatArgs)
    }
}
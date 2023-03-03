package com.otuscoursework.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.otuscoursework.network.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel<S> : ViewModel() {

    abstract var viewModelState: S

    private val _viewModelFlow = MutableSharedFlow<S>()
    val viewModelFlow: SharedFlow<S> = _viewModelFlow

    protected fun S.render() {
        viewModelState = this
        viewModelScope.launch(Dispatchers.Main) {
            _viewModelFlow.emit(viewModelState)
        }
    }
}
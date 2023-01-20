package com.otuscoursework.ui.fragment2

import androidx.lifecycle.viewModelScope
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.utils_and_ext.DEBUG_TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class Fragment2ViewModel : BaseViewModel<Fragment2State>() {

    override var viewModelState = Fragment2State()

    fun onOpen() {
        viewModelScope.launch {
            viewModelState.copy(isLoading = true).render()
            delay(5000)
            Timber.tag(DEBUG_TAG).d("Emit 2")
            viewModelState.copy(
                isLoading = false,
                string = "Hello from fragment 2"
            ).render()
        }
    }
}
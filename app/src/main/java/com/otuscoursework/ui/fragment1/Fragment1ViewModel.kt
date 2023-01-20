package com.otuscoursework.ui.fragment1

import androidx.lifecycle.viewModelScope
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.utils_and_ext.DEBUG_TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class Fragment1ViewModel : BaseViewModel<Fragment1State>() {

    override var viewModelState = Fragment1State()

    fun onOpen() {
        viewModelScope.launch {
            viewModelState.copy(isLoading = true).render()
            delay(5000)
            Timber.tag(DEBUG_TAG).d("Emit")
            viewModelState.copy(
                isLoading = false,
                string = "Hello from fragment 1"
            ).render()
        }
    }
}
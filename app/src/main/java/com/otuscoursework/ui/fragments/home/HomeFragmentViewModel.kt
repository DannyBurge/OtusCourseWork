package com.otuscoursework.ui.fragments.home

import androidx.lifecycle.viewModelScope
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.utils_and_ext.DEBUG_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
) : BaseViewModel<HomeFragmentState>() {

    override var viewModelState = HomeFragmentState()

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
package com.otuscoursework.ui.fragments.menuItemDetail

import androidx.lifecycle.viewModelScope
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import com.otuscoursework.utils_and_ext.DEBUG_TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MenuItemDetailFragmentViewModel @Inject constructor(
) : BaseViewModel<MenuItemDetailFragmentState>() {

    override var viewModelState = MenuItemDetailFragmentState()

    fun onOpen() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelState.copy(isLoading = true).render()

            val stringGotten = ""

            delay(5000)

            if (stringGotten == null) {
                viewModelState.copy(
                    errorMessage = "ERROR",
                    isLoading = false
                ).render()
                return@launch
            }

            Timber.tag(DEBUG_TAG).d("Emit 2")
            viewModelState.copy(
                isLoading = false,
                string = stringGotten
            ).render()
        }
    }
}
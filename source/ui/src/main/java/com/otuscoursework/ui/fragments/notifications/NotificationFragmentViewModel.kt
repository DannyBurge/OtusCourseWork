package com.otuscoursework.ui.fragments.notifications

import androidx.lifecycle.viewModelScope
import com.otuscourcework.network.NetworkRepository
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.BaseViewModel
import com.otuscoursework.ui.fragments.notifications.ui_model.NotificationItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<NotificationFragmentState>() {
    override var viewModelState = NotificationFragmentState()

    fun onOpen() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelState = viewModelState.copy(isLoading = true).apply { render() }

            val balanceHistory = networkRepository.getUserBalanceHistory()

            if (balanceHistory == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            viewModelState = viewModelState.copy(
                isLoading = false,
                balanceList = balanceHistory.map {
                    NotificationItemUiModel(
                        id = it.id ?: 0,
                        orderId = it.orderId ?: 0,
                        date = it.date,
                        amountAdded = it.amountAdded,
                    )
                }.sortedByDescending { it.id }
            ).apply { render() }
        }
    }
}
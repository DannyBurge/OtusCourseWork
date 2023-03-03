package com.otuscoursework.ui.fragments.orders

import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OrdersFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<OrdersFragmentState>() {
    override var viewModelState = OrdersFragmentState()
}
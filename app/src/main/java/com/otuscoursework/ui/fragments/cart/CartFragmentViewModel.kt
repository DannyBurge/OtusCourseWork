package com.otuscoursework.ui.fragments.cart

import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import javax.inject.Inject

class CartFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<CartFragmentState>() {
    override var viewModelState = CartFragmentState()
}
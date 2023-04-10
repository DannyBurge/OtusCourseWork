package com.otuscoursework.ui.fragments.sale

import com.otuscourcework.network.NetworkRepository
import com.otuscoursework.ui.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaleFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<SaleFragmentState>() {
    override var viewModelState = SaleFragmentState()
}
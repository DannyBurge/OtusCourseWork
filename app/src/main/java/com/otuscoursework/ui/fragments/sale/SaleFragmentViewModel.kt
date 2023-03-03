package com.otuscoursework.ui.fragments.sale

import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaleFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<SaleFragmentState>() {
    override var viewModelState = SaleFragmentState()
}
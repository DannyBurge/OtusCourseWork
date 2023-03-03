package com.otuscoursework.ui.fragments.notifications

import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<NotificationFragmentState>() {
    override var viewModelState = NotificationFragmentState()
}
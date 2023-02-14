package com.otuscoursework.ui.fragments.notifications

import androidx.fragment.app.viewModels
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState

class NotificationFragment : BaseFragment<NotificationFragmentViewModel>() {
    override val viewModel: NotificationFragmentViewModel by viewModels()

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

    }
}
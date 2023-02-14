package com.otuscoursework.ui.fragments.orders

import androidx.fragment.app.viewModels
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState

class OrdersFragment : BaseFragment<OrdersFragmentViewModel>() {
    override val viewModel: OrdersFragmentViewModel by viewModels()

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

    }
}
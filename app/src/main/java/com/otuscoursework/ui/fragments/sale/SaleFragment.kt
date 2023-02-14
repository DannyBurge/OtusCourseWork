package com.otuscoursework.ui.fragments.sale

import androidx.fragment.app.viewModels
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState

class SaleFragment : BaseFragment<SaleFragmentViewModel>() {
    override val viewModel: SaleFragmentViewModel by viewModels()

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

    }
}
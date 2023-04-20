package com.otuscoursework.ui.fragments.sale

import android.view.View
import androidx.fragment.app.viewModels
import com.otuscoursework.ui.arch.BaseFragment
import com.otuscoursework.ui.arch.BaseState
import com.otuscoursework.ui.databinding.FragmentSaleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaleFragment(
) : BaseFragment<SaleFragmentViewModel>() {
    override val viewModel: SaleFragmentViewModel by viewModels()

    override lateinit var fragmentBinding: FragmentSaleBinding
    override fun initBinding(): View {
        fragmentBinding = FragmentSaleBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun initViews() {
        //TODO("Not yet implemented")
    }

}
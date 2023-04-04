package com.otuscoursework.ui.fragments.sale

import android.view.View
import androidx.fragment.app.viewModels
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.databinding.FragmentSaleBinding
import com.otuscoursework.di.components.DaggerFragmentSaleComponent
import com.otuscoursework.di.components.FragmentSaleComponent
import com.otuscoursework.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SaleFragment(
) : BaseFragment<SaleFragmentViewModel>() {
    override val viewModel: SaleFragmentViewModel by viewModels()

    override lateinit var fragmentBinding: FragmentSaleBinding
    lateinit var fragmentComponent: FragmentSaleComponent
    override fun initBinding(): View {
        fragmentBinding = FragmentSaleBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun initFragmentComponent() {
        fragmentComponent = DaggerFragmentSaleComponent
            .factory()
            .create(MainActivity.INSTANCE.activityComponent)
        fragmentComponent.inject(this)
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

    }
}
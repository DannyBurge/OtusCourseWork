package com.otuscoursework.ui.fragment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.databinding.Fragment2Binding
import com.otuscoursework.navigation.CiceroneAppNavigator

class Fragment2 : BaseFragment<Fragment2ViewModel>() {

    override val viewModel = Fragment2ViewModel()
    private lateinit var fragmentBinding: Fragment2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentBinding = Fragment2Binding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun initViews() {
        fragmentBinding.navigateButton.setOnClickListener {
            CiceroneAppNavigator.toFragment1()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onOpen()
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as Fragment2State).apply {
            fragmentBinding.fragment1TextView.text = state.string
        }
    }
}
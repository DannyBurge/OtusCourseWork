package com.otuscoursework.ui.fragment1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.databinding.Fragment1Binding
import com.otuscoursework.navigation.CiceroneAppNavigator

class Fragment1 : BaseFragment<Fragment1ViewModel>() {

    private lateinit var fragmentBinding: Fragment1Binding

    override val viewModel = Fragment1ViewModel()

    override fun initViews() {
        fragmentBinding.navigateButton.setOnClickListener {
            CiceroneAppNavigator.toFragment2()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Fragment1State(isLoading = false)
        fragmentBinding = Fragment1Binding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onOpen()
    }


    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as Fragment1State).apply {
            fragmentBinding.fragment1TextView.text = state.string
        }
    }
}
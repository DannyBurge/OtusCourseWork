package com.otuscoursework.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.otuscoursework.OtusApplication
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.databinding.Fragment1Binding
import com.otuscoursework.di.components.ActivityComponent
import com.otuscoursework.di.components.DaggerActivityComponent
import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.main.MainActivity
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeFragmentViewModel>() {

    private lateinit var fragmentBinding: Fragment1Binding
    override val viewModel: HomeFragmentViewModel by viewModels()


    override fun initViews() {
        fragmentBinding.navigateButton.setOnClickListener {
            ciceroneAppNavigator.toMenuItemDetailScreen()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        HomeFragmentState(isLoading = false)
        fragmentComponent.inject(this)
        fragmentBinding = Fragment1Binding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onOpen()
    }


    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as HomeFragmentState).apply {
            fragmentBinding.fragment1TextView.text = state.string
        }
    }
}
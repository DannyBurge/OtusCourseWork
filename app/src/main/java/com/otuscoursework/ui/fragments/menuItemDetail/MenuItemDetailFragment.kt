package com.otuscoursework.ui.fragments.menuItemDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.databinding.Fragment2Binding
import com.otuscoursework.navigation.CiceroneAppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class MenuItemDetailFragment : BaseFragment<MenuItemDetailFragmentViewModel>() {

    override val viewModel: MenuItemDetailFragmentViewModel by viewModels()

    private lateinit var fragmentBinding: Fragment2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentComponent.inject(this)
        fragmentBinding = Fragment2Binding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun initViews() {
        fragmentBinding.navigateButton.setOnClickListener {
            ciceroneAppNavigator.toHomeScreen()
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.onOpen()
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as MenuItemDetailFragmentState).apply {
            fragmentBinding.fragment1TextView.text = state.string
        }
    }
}
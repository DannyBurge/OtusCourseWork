package com.otuscoursework.arch

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.main.MainActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<out T : BaseViewModel<*>> : Fragment() {

    abstract val viewModel: T

    @Inject
    lateinit var ciceroneAppNavigator: CiceroneAppNavigator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initViews()
    }

    abstract fun initViews()

    @CallSuper
    open fun initObservers() {
        lifecycleScope.launch {
            viewModel.viewModelFlow.collect {
                renderState(it as BaseState)
            }
        }
    }

    @CallSuper
    open fun renderState(state: BaseState) {
        if (state.isLoading) showLoading() else hideLoading()
        if (state.errorMessage != null) showError(state.errorMessage!!)
    }

    private fun showError(msg: String) {
        (activity as MainActivity).showError(msg)
    }

    private fun showLoading() {
        (activity as MainActivity).showLoading()
    }

    private fun hideLoading() {
        (activity as MainActivity).hideLoading()
    }
}
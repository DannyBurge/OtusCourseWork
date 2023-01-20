package com.otuscoursework.arch

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.otuscoursework.ui.main.MainActivity
import kotlinx.coroutines.launch

abstract class BaseFragment<out T : BaseViewModel<*>> : Fragment() {

    abstract val viewModel: T

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initViews()
    }

    abstract fun initViews()

    private fun initObservers() {
        lifecycleScope.launch {
            viewModel.viewModelFlow.collect {
                renderState(it as BaseState)
            }
        }
    }

    @CallSuper
    open fun renderState(state: BaseState) {
        if (state.isLoading) showLoading() else hideLoading()
        if (state.errorMessage != null) showError()
    }

    private fun showError() {

    }

    private fun showLoading() {
        (activity as MainActivity).showLoading()
    }

    private fun hideLoading() {
        (activity as MainActivity).hideLoading()
    }
}
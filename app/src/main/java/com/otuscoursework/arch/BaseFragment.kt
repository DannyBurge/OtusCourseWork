package com.otuscoursework.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.main.MainActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<out V : BaseViewModel<*>> : Fragment() {

    abstract val viewModel: V
    abstract val fragmentBinding: ViewBinding

    @Inject
    lateinit var ciceroneAppNavigator: CiceroneAppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragmentComponent()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = initBinding()
        renderState(viewModel.viewModelState as BaseState)
        return view
    }

    abstract fun initBinding(): View

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initViews()
    }

    abstract fun initViews()
    abstract fun initFragmentComponent()

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
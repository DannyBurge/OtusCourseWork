package com.otuscoursework.ui.arch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.otuscourcework.utils.OtusLogger
import com.otuscoursework.ui.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.views.loading_dialog.OtusLoadingDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseFragment<out V : BaseViewModel<*>> : Fragment() {

    abstract val viewModel: V
    abstract val fragmentBinding: ViewBinding

    @Inject
    lateinit var ciceroneAppNavigator: CiceroneAppNavigator
    @Inject
    lateinit var otusLogger: OtusLogger

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
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    private val progressDialog = OtusLoadingDialog()
    private fun showLoading() {
        otusLogger.log("showLoading")
        if (progressDialog.isShown) hideLoading()
        progressDialog.show()
    }

    private fun hideLoading() {
        otusLogger.log("hideLoading")
        progressDialog.dismiss()
    }
}
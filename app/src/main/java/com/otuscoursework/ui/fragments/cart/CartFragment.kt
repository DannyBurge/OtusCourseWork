package com.otuscoursework.ui.fragments.cart

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.models.CartCheckItemUiModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartFragment : BaseFragment<CartFragmentViewModel>() {
    override val viewModel: CartFragmentViewModel by viewModels()

    @Inject
    lateinit var cartKeeper: CartKeeper

    override fun initObservers() {
        super.initObservers()

        lifecycleScope.launch {
            cartKeeper.getFlow().collect {
                populateCartList(it)
            }
        }
    }

    private fun populateCartList(cartCheckItemUiModelList: List<CartCheckItemUiModel>) {

    }

    override fun initViews() {
        //TODO("Not yet implemented")
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

    }
}
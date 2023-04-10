package com.otuscoursework.ui.fragments.cart

import com.otuscoursework.ui.arch.BaseState
import com.otuscoursework.ui.fragments.cart.ui_model.AddressItemUiModel
import com.otuscoursework.ui.fragments.cart.ui_model.CartItemUiModel

data class CartFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,

    var sentOrder: Int? = null,
    val cartItemList: List<CartItemUiModel> = emptyList(),
    val addressItemList: MutableList<AddressItemUiModel> = mutableListOf(),
) : BaseState
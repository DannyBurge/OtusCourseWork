package com.otuscoursework.ui.fragments.cart

import com.otuscoursework.arch.BaseState
import com.otuscoursework.ui.models.AddressItemUiModel
import com.otuscoursework.ui.models.CartItemUiModel

data class CartFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,

    var sentOrder: Int? = null,
    val cartItemList: List<CartItemUiModel> = emptyList(),
    val addressItemList: MutableList<AddressItemUiModel> = mutableListOf(),
) : BaseState
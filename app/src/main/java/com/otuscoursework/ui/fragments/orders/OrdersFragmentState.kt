package com.otuscoursework.ui.fragments.orders

import com.otuscoursework.arch.BaseState
import com.otuscoursework.ui.models.OrderItemUiModel

data class OrdersFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val orderList: List<OrderItemUiModel> = emptyList()
) : BaseState
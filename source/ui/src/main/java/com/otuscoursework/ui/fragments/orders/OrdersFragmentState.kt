package com.otuscoursework.ui.fragments.orders

import com.otuscoursework.ui.arch.BaseState
import com.otuscoursework.ui.fragments.orders.ui_model.OrderItemUiModel

data class OrdersFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val orderList: List<OrderItemUiModel> = emptyList()
) : BaseState
package com.otuscoursework.ui.fragments.orders

import androidx.lifecycle.viewModelScope
import com.otuscourcework.network.NetworkRepository
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.BaseViewModel
import com.otuscoursework.ui.fragments.cart.ui_model.CartItemUiModel
import com.otuscoursework.ui.fragments.orders.ui_model.OrderItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<OrdersFragmentState>() {
    override var viewModelState = OrdersFragmentState()

    fun onOpen(openedOrderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelState = viewModelState.copy(isLoading = true).apply { render() }

            val orders = networkRepository.getUserOrders()

            if (orders == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            val orderItemList: MutableList<OrderItemUiModel> = mutableListOf()

            val orderItemGroupIds = orders.flatMap { order -> order.positions.map { it.groupId } }

            val positions = networkRepository.getMenu(orderItemGroupIds)

            if (positions == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            orders.map { order ->

                val cartItemList = order.positions.map { orderPosition ->

                    val name = positions.first { it.groupId == orderPosition.groupId }.name

                    val sizeName = positions.first {
                        it.groupId == orderPosition.groupId
                    }.subItems.first {
                        it.id == orderPosition.sizeId
                    }.displayName

                    val price = positions.first {
                        it.groupId == orderPosition.groupId
                    }.subItems.first {
                        it.id == orderPosition.sizeId
                    }.price

                    val amount = order.positions.first {
                        it.groupId == orderPosition.groupId
                    }.amount

                    CartItemUiModel(
                        id = orderPosition.groupId,
                        name = "$name, $sizeName",
                        price = price,
                        amount = amount
                    )
                }

                val isOpen = if (openedOrderId > OrdersFragment.EMPTY_ORDER_ID) {
                    order.id == openedOrderId
                } else {
                    order.isActive
                }

                orderItemList.add(
                    OrderItemUiModel(
                        id = order.id ?: OrdersFragment.EMPTY_ORDER_ID,
                        isActive = order.isActive,
                        isOpen = isOpen,
                        date = order.date,
                        orderCheckItemList = cartItemList,
                        totalPrice = order.price,
                        tokensAdded = order.tokensAmount
                    )
                )
            }

            viewModelState = viewModelState.copy(
                isLoading = false,
                orderList = orderItemList.sortedByDescending { it.id }
            ).apply { render() }
        }
    }
}
package com.otuscoursework.ui

import com.otuscoursework.ui.models.CartCheckItemUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class CartKeeper @Inject constructor() {

    private val _cartContent = MutableStateFlow<List<CartCheckItemUiModel>>(mutableListOf())
    private val cartContent: StateFlow<List<CartCheckItemUiModel>> = _cartContent

    suspend fun addItemToCart(newItem: CartCheckItemUiModel) {
        val updatedList = _cartContent.value + newItem
        _cartContent.emit(updatedList)
    }

    suspend fun removeItemFromCart(item: CartCheckItemUiModel) {
        val updatedList = _cartContent.value.toMutableList()
        updatedList.remove(item)

        _cartContent.emit(updatedList)
    }

    suspend fun clearCart() {
        _cartContent.emit(emptyList())
    }

    fun getFlow(): SharedFlow<List<CartCheckItemUiModel>> {
        return cartContent
    }

}
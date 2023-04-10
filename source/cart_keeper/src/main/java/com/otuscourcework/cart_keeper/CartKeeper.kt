package com.otuscourcework.cart_keeper

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class CartKeeper @Inject constructor() {

    private val cartContent: HashMap<Int, CartItemModel> = hashMapOf()

    private val _cartSize = MutableStateFlow(0)
    val cartSize: StateFlow<Int> = _cartSize

    suspend fun addItemToCart(item: CartItemModel) {
        val itemKey = item.id

        if (cartContent[itemKey] == null) {
            cartContent[itemKey] = item
        } else {
            cartContent[itemKey] = cartContent[itemKey]!!.copy(
                amount = cartContent[itemKey]!!.amount + ONE_ITEM
            )
        }
        _cartSize.emit(cartContent.map { it.value.amount }.sum())
    }

    suspend fun removeItemFromCart(id: Int) {
        if (cartContent[id] == null) return
        cartContent[id] = cartContent[id]!!.copy(amount = cartContent[id]!!.amount - ONE_ITEM)
        if (cartContent[id]!!.amount == 0) cartContent.remove(id)
        _cartSize.emit(cartContent.map { it.value.amount }.sum())
    }

    suspend fun clearCart() {
        cartContent.clear()
        _cartSize.emit(0)
    }

    fun getCartContent(): HashMap<Int, CartItemModel> {
        return cartContent
    }

    fun getTotalPrice(): Int {
        return cartContent.map { it.value.price * it.value.amount }.sum()
    }

    companion object {
        const val ONE_ITEM = 1
    }
}
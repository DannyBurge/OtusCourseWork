package com.otuscoursework.ui

import com.otuscoursework.ui.models.CartItemModel
import com.otuscoursework.ui.models.MenuItemDetailModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class CartKeeper @Inject constructor() {

    private val _cartContent =
        MutableStateFlow<HashMap<Int, CartItemModel>>(hashMapOf())
    val cartFlow: StateFlow<HashMap<Int, CartItemModel>> = _cartContent

    suspend fun addItemToCart(item: MenuItemDetailModel, selectedSizeIndex: Int) {
        val itemKey = item.subItems[selectedSizeIndex].id
        val cartItems = getSnapshot().clone() as HashMap<Int, CartItemModel>

        if (cartItems[itemKey] == null) {
            cartItems[itemKey] = CartItemModel(
                groupId = item.groupId,
                name = item.name,
                subName = item.subItems[selectedSizeIndex].displayName,
                id = item.subItems[selectedSizeIndex].id,
                price = item.subItems[selectedSizeIndex].price,
                amount = ONE_ITEM
            )
        } else {
            cartItems[itemKey] =
                cartItems[itemKey]!!.copy(amount = cartItems[itemKey]!!.amount + ONE_ITEM)
        }
        _cartContent.emit(cartItems)
    }

    suspend fun removeItemFromCart(id: Int) {
        val cartItems = getSnapshot().clone() as HashMap<Int, CartItemModel>
        if (cartItems[id] == null) return
        cartItems[id] =
            cartItems[id]!!.copy(amount = cartItems[id]!!.amount - ONE_ITEM)
        if (cartItems[id]!!.amount == 0) cartItems.remove(id)
        _cartContent.emit(cartItems)
    }

    suspend fun clearCart() {
        _cartContent.emit(hashMapOf())
    }

    fun getSnapshot(): HashMap<Int, CartItemModel> {
        return _cartContent.value
    }

    fun getTotalPrice(): Int {
        return _cartContent.value.map { it.value.price * it.value.amount }.sum()
    }

    companion object {
        private const val ONE_ITEM = 1
    }
}
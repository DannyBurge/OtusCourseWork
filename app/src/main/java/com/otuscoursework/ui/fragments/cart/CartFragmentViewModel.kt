package com.otuscoursework.ui.fragments.cart

import androidx.lifecycle.viewModelScope
import com.otuscoursework.R
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import com.otuscoursework.network.models.OrderItem
import com.otuscoursework.network.models.OrderPosition
import com.otuscoursework.network.models.UserDeliveryAddress
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.UserDataKeeper
import com.otuscoursework.ui.models.AddressItemUiModel
import com.otuscoursework.ui.models.CartItemUiModel
import com.otuscoursework.utils_and_ext.toFullIsoDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class CartFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<CartFragmentState>() {
    override var viewModelState = CartFragmentState()

    @Inject
    lateinit var cartKeeper: CartKeeper

    @Inject
    lateinit var userDataKeeper: UserDataKeeper

    var tokensSpendOnOrder = 0

    fun getUserAddresses() {
        viewModelScope.launch(Dispatchers.IO) {
            val userDeliveryAddressList = networkRepository.getUserAddresses()

            if (userDeliveryAddressList == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            viewModelState = viewModelState.copy(
                isLoading = false,
                addressItemList = userDeliveryAddressList.map {
                    AddressItemUiModel(
                        id = it.id ?: 0,
                        displayName = it.name,
                        address = it.address
                    )
                } as MutableList<AddressItemUiModel>
            ).apply { render() }
        }
    }

    fun getCartItems() {
        val cartItems = cartKeeper.getSnapshot()
        val cartUi = cartItems.keys.map {
            CartItemUiModel(
                id = cartItems[it]!!.id,
                name = "${cartItems[it]!!.name}, ${cartItems[it]!!.subName}",
                price = cartItems[it]!!.price,
                amount = cartItems[it]!!.amount
            )
        }
        tokensSpendOnOrder = getMaxTokensAddition()
        viewModelState = viewModelState.copy(
            cartItemList = cartUi
        ).apply {
            render()
        }
    }

    fun sendOrder() {
        val positionList = cartKeeper.getSnapshot()
        viewModelScope.launch(Dispatchers.IO) {
            val orderItem = OrderItem(
                price = cartKeeper.getTotalPrice(),
                tokensAmount = tokensSpendOnOrder,
                address = userDataKeeper.address,
                date = Date().toFullIsoDate(),
                isActive = true,
                positions = positionList.map {
                    OrderPosition(
                        groupId = it.value.groupId,
                        sizeId = it.value.id,
                        amount = it.value.amount,
                    )
                }
            )
            val order = networkRepository.sendOrder(orderItem)
            if (order == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            viewModelState = viewModelState.copy(
                isLoading = false,
                sentOrder = order.id
            ).apply { render() }

            cartKeeper.clearCart()
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartKeeper.clearCart()
            getCartItems()
        }
    }

    fun getMaxTokensAddition(): Int {
        return cartKeeper.getTotalPrice() / 10
    }

    fun getMaxDiscount(): Int {
        return min(userDataKeeper.tokensAmount ?: 0, cartKeeper.getTotalPrice())
    }

    fun getTotalPrice(): Int {
        return cartKeeper.getTotalPrice()
    }

    fun getAddressById(id: Int): AddressItemUiModel? {
        return viewModelState.addressItemList.firstOrNull { it.id == id }
    }

    fun changeCurrentAddress(addressItemUiModel: AddressItemUiModel) {
        userDataKeeper.address = addressItemUiModel.address
    }

    fun sendNewAddress(address: UserDeliveryAddress) {
        viewModelScope.launch(Dispatchers.IO) {
            val newAddress = networkRepository.sendNewAddress(address)

            if (newAddress == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            val newUiAddress = AddressItemUiModel(
                id = newAddress.id ?: 0,
                displayName = newAddress.name,
                address = newAddress.address
            )

            changeCurrentAddress(newUiAddress)

            viewModelState = viewModelState.copy(
                isLoading = false,
                addressItemList = viewModelState.addressItemList.also {
                    it.add(newUiAddress)
                }
            ).apply { render() }
        }
    }
}
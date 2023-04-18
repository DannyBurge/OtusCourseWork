package com.otuscoursework.ui.fragments.cart

import androidx.lifecycle.viewModelScope
import com.otuscourcework.cart_keeper.CartKeeper
import com.otuscourcework.network.NetworkRepository
import com.otuscourcework.network.models.OrderItem
import com.otuscourcework.network.models.OrderPosition
import com.otuscourcework.network.models.UserDeliveryAddress
import com.otuscourcework.utils.toFullIsoDate
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.BaseViewModel
import com.otuscoursework.ui.fragments.cart.ui_model.AddressItemUiModel
import com.otuscoursework.ui.fragments.cart.ui_model.CartItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class CartFragmentViewModel @Inject constructor(
    private val networkRepository: NetworkRepository
) : BaseViewModel<CartFragmentState>() {
    override var viewModelState = CartFragmentState()

    @Inject
    lateinit var cartKeeper: CartKeeper

    var tokensSpendOnOrder = 0

    fun getUserAddresses() {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelState = viewModelState.copy(isLoading = true).apply { render() }

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
        viewModelState = viewModelState.copy(isLoading = true).apply { render() }
        val cartItems = cartKeeper.getCartContent()
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
            isLoading = false,
            cartItemList = cartUi
        ).apply {
            render()
        }
    }

    fun sendOrder() {
        val positionList = cartKeeper.getCartContent()
        viewModelScope.launch(Dispatchers.IO) {

            viewModelState = viewModelState.copy(isLoading = true).apply { render() }

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
            viewModelState = viewModelState.copy(isLoading = true).apply { render() }
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
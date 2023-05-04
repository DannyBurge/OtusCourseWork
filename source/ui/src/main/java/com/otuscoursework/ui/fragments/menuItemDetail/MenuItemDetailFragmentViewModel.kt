package com.otuscoursework.ui.fragments.menuItemDetail

import androidx.lifecycle.viewModelScope
import com.otuscourcework.network.NetworkRepository
import com.otuscoursework.ui.arch.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemDetailFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<MenuItemDetailFragmentState>() {

    @Inject
    lateinit var cartKeeper: com.otuscourcework.cart_keeper.CartKeeper

    override var viewModelState = MenuItemDetailFragmentState()

    fun addItemToCart(menuItem: MenuItemDetailModel, selectedSizeIndex: Int) {
        viewModelScope.launch {
            cartKeeper.addItemToCart(
                com.otuscourcework.cart_keeper.CartItemModel(
                    groupId = menuItem.groupId,
                    name = menuItem.name,
                    subName = menuItem.subItems[selectedSizeIndex].displayName,
                    id = menuItem.subItems[selectedSizeIndex].id,
                    price = menuItem.subItems[selectedSizeIndex].price,
                    amount = com.otuscourcework.cart_keeper.CartKeeper.ONE_ITEM
                )
            )
        }
    }

    fun removeItemFromCart(menuItem: MenuItemDetailModel, selectedSizeIndex: Int) {
        viewModelScope.launch {
            cartKeeper.removeItemFromCart(id = menuItem.subItems[selectedSizeIndex].id)
        }
    }
}
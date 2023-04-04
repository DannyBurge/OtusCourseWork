package com.otuscoursework.ui.fragments.menuItemDetail

import androidx.lifecycle.viewModelScope
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.models.MenuItemDetailModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuItemDetailFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<MenuItemDetailFragmentState>() {

    @Inject
    lateinit var cartKeeper: CartKeeper

    override var viewModelState = MenuItemDetailFragmentState()

    fun addItemToCart(menuItem: MenuItemDetailModel, selectedSizeIndex: Int) {
        viewModelScope.launch {
            cartKeeper.addItemToCart(menuItem, selectedSizeIndex)
        }
    }

    fun removeItemFromCart(menuItem: MenuItemDetailModel, selectedSizeIndex: Int) {
        viewModelScope.launch {
            cartKeeper.removeItemFromCart(id = menuItem.subItems[selectedSizeIndex].id)
        }
    }
}
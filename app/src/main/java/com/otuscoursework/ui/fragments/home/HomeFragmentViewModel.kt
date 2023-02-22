package com.otuscoursework.ui.fragments.home

import androidx.lifecycle.viewModelScope
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.models.CategoryItem
import com.otuscoursework.network.models.MenuItem
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.ui.models.MenuItemSizeUiModel
import com.otuscoursework.ui.models.MenuItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
) : BaseViewModel<HomeFragmentState>() {

    override var viewModelState = HomeFragmentState()

    fun onOpen() {
        viewModelScope.launch {
            viewModelState.copy(isLoading = true).render()

            val balanceCall = async { networkRepository.getUserBalanceHistory(true) }
            val chipsCall = async { networkRepository.getCategories() }
            val menuCall = async { networkRepository.getMenu() }

            val balance = balanceCall.await()
            val chips = chipsCall.await()
            val menu = menuCall.await()

            if (balance == null || chips == null || menu == null) {
                viewModelState.copy(isLoading = false, errorMessage = "ERROR").render()
                return@launch
            }
            viewModelState.copy(
                isLoading = false,
                tokenAmount = balance.last().amount,
                chipsItemList = chips.toUiChips(),
                menuItemList = menu.toUiMenu(),
            ).render()
        }
    }

    private fun List<CategoryItem>.toUiChips(): List<ChipItemUiModel> {
        return this.map {
            ChipItemUiModel(
                id = it.id,
                name = it.name,
                isSelected = false
            )
        }
    }
    private fun List<MenuItem>.toUiMenu(): List<MenuItemUiModel> {
        return this.map { menuItem ->
            val sizes = menuItem.sizes.map { size ->
                MenuItemSizeUiModel(
                    sizeId = size.sizeId,
                    price = size.price,
                    displayName = size.displayName
                )
            }
            MenuItemUiModel(
                id = menuItem.id,
                name = menuItem.name,
                categoryId = menuItem.categoryId,
                picture = menuItem.picture,
                description = menuItem.description,
                sizes = sizes,
                isInFavourite = false
            )
        }
    }
}
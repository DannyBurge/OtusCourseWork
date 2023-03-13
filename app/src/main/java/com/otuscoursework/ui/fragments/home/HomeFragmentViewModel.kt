package com.otuscoursework.ui.fragments.home

import androidx.lifecycle.viewModelScope
import com.otuscoursework.arch.BaseViewModel
import com.otuscoursework.network.NetworkRepository
import com.otuscoursework.network.models.*
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.ui.models.MenuItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<HomeFragmentState>() {

    override var viewModelState = HomeFragmentState()

    fun onOpen() {
        viewModelScope.launch {
            viewModelState.copy(isLoading = true).render()

            val balanceCall = async { networkRepository.getUserBalanceHistory(true) }
            val chipsCall = async { networkRepository.getCategories() }
            val menuCall = async { networkRepository.getMenu() }

            val balance = balanceCall.await() ?: listOf(
                BalanceHistoryItem(
                    id = 0,
                    orderId = 111,
                    date = Date().toString(),
                    amountAdded = 20,
                    amount = 1234
                )
            )
            val chips = chipsCall.await() ?: listOf(
                CategoryItem(id = 0, name = "Пицца"),
                CategoryItem(id = 1, name = "Закуски"),
                CategoryItem(id = 2, name = "Десерты"),
                CategoryItem(id = 3, name = "Соусы"),
                CategoryItem(id = 4, name = "Напитки"),
            )
            val menu = menuCall.await() ?: listOf(
                MenuItem(
                    id = 0,
                    categoryId = 0,
                    picture = "",
                    name = "Пицца 1",
                    description = "Это пицца 1",
                    sizes = listOf(
                        MenuItemSize(sizeId = 0, 100, "10 см", MenuItemFoodValue("100", "101", "102", "103", "104")),
                        MenuItemSize(sizeId = 1, 200, "20 см", MenuItemFoodValue("200", "201", "202", "203", "204")),
                        MenuItemSize(sizeId = 2, 300, "30 см", MenuItemFoodValue("300", "301", "302", "303", "304"))
                    ),
                    foodValue = MenuItemFoodValue("100", "101", "102", "103", "104")
                ),
                MenuItem(
                    id = 1,
                    categoryId = 0,
                    picture = "",
                    name = "Пицца 2",
                    description = "Это пицца 2",
                    sizes = listOf(
                        MenuItemSize(sizeId = 0, 100, "10 см", MenuItemFoodValue("100", "101", "102", "103", "104")),
                        MenuItemSize(sizeId = 1, 200, "20 см", MenuItemFoodValue("200", "201", "202", "203", "204")),
                        MenuItemSize(sizeId = 2, 300, "30 см", MenuItemFoodValue("300", "301", "302", "303", "304"))
                    ),
                    foodValue = MenuItemFoodValue("100", "101", "102", "103", "104")
                ),
                MenuItem(
                    id = 2,
                    categoryId = 0,
                    picture = "",
                    name = "Пицца 3",
                    description = "Это пицца 3",
                    sizes = listOf(
                        MenuItemSize(sizeId = 0, 100, "10 см", MenuItemFoodValue("100", "101", "102", "103", "104")),
                        MenuItemSize(sizeId = 1, 200, "20 см", MenuItemFoodValue("200", "201", "202", "203", "204")),
                        MenuItemSize(sizeId = 2, 300, "30 см", MenuItemFoodValue("300", "301", "302", "303", "304"))
                    ),
                    foodValue = MenuItemFoodValue("100", "101", "102", "103", "104")
                ),
                MenuItem(
                    id = 3,
                    categoryId = 0,
                    picture = "",
                    name = "Пицца 4",
                    description = "Это пицца 4",
                    sizes = listOf(
                        MenuItemSize(sizeId = 0, 100, "10 см", MenuItemFoodValue("100", "101", "102", "103", "104")),
                        MenuItemSize(sizeId = 1, 200, "20 см", MenuItemFoodValue("200", "201", "202", "203", "204")),
                        MenuItemSize(sizeId = 2, 300, "30 см", MenuItemFoodValue("300", "301", "302", "303", "304"))
                    ),
                    foodValue = MenuItemFoodValue("100", "101", "102", "103", "104")
                ),
                MenuItem(
                    id = 4,
                    categoryId = 0,
                    picture = "",
                    name = "Пицца 5",
                    description = "Это пицца 5",
                    sizes = listOf(
                        MenuItemSize(sizeId = 0, 100, "10 см", MenuItemFoodValue("100", "101", "102", "103", "104")),
                        MenuItemSize(sizeId = 1, 200, "20 см", MenuItemFoodValue("200", "201", "202", "203", "204")),
                        MenuItemSize(sizeId = 2, 300, "30 см", MenuItemFoodValue("300", "301", "302", "303", "304"))
                    ),
                    foodValue = MenuItemFoodValue("100", "101", "102", "103", "104")
                ),
                MenuItem(
                    id = 5,
                    categoryId = 0,
                    picture = "",
                    name = "Пицца 6",
                    description = "Это пицца 6",
                    sizes = listOf(
                        MenuItemSize(sizeId = 0, 100, "10 см", MenuItemFoodValue("100", "101", "102", "103", "104")),
                        MenuItemSize(sizeId = 1, 200, "20 см", MenuItemFoodValue("200", "201", "202", "203", "204")),
                        MenuItemSize(sizeId = 2, 300, "30 см", MenuItemFoodValue("300", "301", "302", "303", "304"))
                    ),
                    foodValue = MenuItemFoodValue("100", "101", "102", "103", "104")
                ),
                MenuItem(
                    id = 6,
                    categoryId = 0,
                    picture = "",
                    name = "Пицца 7",
                    description = "Это пицца 7",
                    sizes = listOf(
                        MenuItemSize(sizeId = 0, 100, "10 см", MenuItemFoodValue("100", "101", "102", "103", "104")),
                        MenuItemSize(sizeId = 1, 200, "20 см", MenuItemFoodValue("200", "201", "202", "203", "204")),
                        MenuItemSize(sizeId = 2, 300, "30 см", MenuItemFoodValue("300", "301", "302", "303", "304"))
                    ),
                    foodValue = MenuItemFoodValue("100", "101", "102", "103", "104")
                ),
            )

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

    private fun List<MenuItem>.toUiMenu(): List<MenuItemModel> {
        return this.map { menuItem ->
            MenuItemModel.from(menuItem)
        }
    }
}
package com.otuscoursework.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.otuscourcework.network.NetworkRepository
import com.otuscourcework.network.models.*
import com.otuscourcework.utils.toFullIsoDate
import com.otuscoursework.ui.fragments.cart.ui_model.CartItemUiModel
import com.otuscoursework.ui.fragments.orders.OrdersFragmentViewModel
import com.otuscoursework.ui.fragments.orders.ui_model.OrderItemUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class OrdersFragmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val networkRepository: NetworkRepository = mock()

    private val viewModel = OrdersFragmentViewModel(networkRepository)

    @Before
    fun setup() {
        whenever(networkRepository.getMenu(anyList())).thenReturn(
            mockedMenuItemList
        )

        whenever(networkRepository.getUserOrders()).thenReturn(
            mockedOrderItemList
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should get correct items`() = runTest {
        withContext(Dispatchers.Default) { viewModel.onOpen(-1) }
        val isEquals = viewModel.viewModelState.orderList == testOrderItemList
        assert(isEquals)
    }


    companion object {
        private val currentDate = Date().toFullIsoDate()

        private val testOrderItemUiModel1 = OrderItemUiModel(
            id = 0,
            isActive = false,
            isOpen = false,
            date = currentDate,
            orderCheckItemList = listOf(
                CartItemUiModel(0, "Пицца 1, 23 см", 470, 1),
                CartItemUiModel(0, "Пицца 1, 30 см", 670, 1),
                CartItemUiModel(0, "Пицца 1, 35 см", 840, 1),
            ),
            tokensAdded = 0,
            totalPrice = 1980
        )

        private val testOrderItemUiModel2 = OrderItemUiModel(
            id = 1,
            isActive = true,
            isOpen = true,
            date = currentDate,
            orderCheckItemList = listOf(
                CartItemUiModel(1, "Пицца 2, 23 см", 470, 1),
                CartItemUiModel(1, "Пицца 2, 30 см", 670, 1),
                CartItemUiModel(1, "Пицца 2, 35 см", 840, 1),
            ),
            tokensAdded = 0,
            totalPrice = 1980
        )

        private val orderItem1 = OrderItem(
            0,
            1980,
            0,
            "address_1",
            currentDate,
            false,
            listOf(
                OrderPosition(0, 0, 1),
                OrderPosition(0, 1, 1),
                OrderPosition(0, 2, 1),
            )
        )

        private val orderItem2 = OrderItem(
            1,
            1980,
            0,
            "address_1",
            currentDate,
            true,
            listOf(
                OrderPosition(1, 3, 1),
                OrderPosition(1, 4, 1),
                OrderPosition(1, 5, 1),
            )
        )

        private val menuItem1 = MenuItem(
            groupId = 0,
            categoryId = 0,
            picture = "",
            name = "Пицца 1",
            description = "Описание пиццы 1",
            foodValue = MenuItemFoodValue("243", "11", "12", "22"),
            subItems = listOf(
                MenuSubItem(0, 470, "23 см", 360),
                MenuSubItem(1, 670, "30 см", 600),
                MenuSubItem(2, 840, "35 см", 700)
            )
        )

        private val menuItem2 = MenuItem(
            groupId = 1,
            categoryId = 0,
            picture = "",
            name = "Пицца 2",
            description = "Описание пиццы 2",
            foodValue = MenuItemFoodValue("243", "11", "12", "22"),
            subItems = listOf(
                MenuSubItem(3, 470, "23 см", 360),
                MenuSubItem(4, 670, "30 см", 600),
                MenuSubItem(5, 840, "35 см", 700)
            )
        )

        private val menuItem3 = MenuItem(
            groupId = 2,
            categoryId = 0,
            picture = "",
            name = "Пицца 2",
            description = "Описание пиццы 2",
            foodValue = MenuItemFoodValue("243", "11", "12", "22"),
            subItems = listOf(
                MenuSubItem(6, 470, "23 см", 360),
                MenuSubItem(7, 670, "30 см", 600),
                MenuSubItem(8, 840, "35 см", 700)
            )
        )

        private val testOrderItemList = listOf(testOrderItemUiModel2, testOrderItemUiModel1)

        private val mockedMenuItemList = listOf(menuItem1, menuItem2, menuItem3)
        private val mockedOrderItemList = listOf(orderItem1, orderItem2)
    }
}
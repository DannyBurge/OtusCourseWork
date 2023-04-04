package com.otuscoursework.navigation

import com.github.terrakok.cicerone.Router
import com.otuscoursework.ui.fragments.orders.OrdersFragment
import com.otuscoursework.ui.models.MenuItemUiModel
import javax.inject.Inject

class CiceroneAppNavigator @Inject constructor(
    private val appRouter: Router
) {
    fun toHomeScreen() {
        appRouter.navigateTo(Screens.homeScreen())
    }

    fun toCartScreen() {
        appRouter.navigateTo(Screens.cartScreen())
    }

    fun toOrdersScreen(orderId: Int = OrdersFragment.EMPTY_ORDER_ID) {
        appRouter.navigateTo(Screens.ordersScreen(orderId))
    }

    fun toNotificationScreen() {
        appRouter.navigateTo(Screens.notificationsScreen())
    }

    fun toSaleScreen() {
        appRouter.navigateTo(Screens.saleScreen())
    }

    fun back() {
        appRouter.exit()
    }
}

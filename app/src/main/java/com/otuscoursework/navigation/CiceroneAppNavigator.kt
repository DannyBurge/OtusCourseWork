package com.otuscoursework.navigation

import com.github.terrakok.cicerone.Router
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

    fun toOrdersScreen() {
        appRouter.navigateTo(Screens.ordersScreen())
    }

    fun toNotificationScreen(orderId: Int = 0) {
        appRouter.navigateTo(Screens.notificationsScreen(orderId))
    }

    fun toSaleScreen() {
        appRouter.navigateTo(Screens.saleScreen())
    }

    fun back() {
        appRouter.exit()
    }
}

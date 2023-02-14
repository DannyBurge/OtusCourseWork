package com.otuscoursework.navigation

import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class CiceroneAppNavigator @Inject constructor(
    private val appRouter: Router
) {
    fun toHomeScreen() {
        appRouter.navigateTo(Screens.homeScreen())
    }

    fun toMenuItemDetailScreen() {
        appRouter.navigateTo(Screens.menuItemDetailScreen())
    }

    fun toCartScreen() {
        appRouter.navigateTo(Screens.cartScreen())
    }

    fun toOrdersScreen() {
        appRouter.navigateTo(Screens.ordersScreen())
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

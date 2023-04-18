package com.otuscoursework.ui.navigation

import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class CiceroneAppNavigator @Inject constructor(
    private val appRouter: Router
) {
    fun toAuthScreen(isCreatePasswordMode: Boolean = false) {
        appRouter.navigateTo(Screens.authScreen(isCreatePasswordMode))
    }

    fun toHomeScreen() {
        appRouter.navigateTo(Screens.homeScreen())
    }

    fun toCartScreen() {
        appRouter.navigateTo(Screens.cartScreen())
    }

    fun toOrdersScreen(orderId: Int = -1) {
        appRouter.navigateTo(Screens.ordersScreen(orderId))
    }

    fun toNotificationScreen() {
        appRouter.navigateTo(Screens.notificationsScreen())
    }

    fun toSettingsScreen() {
        appRouter.navigateTo(Screens.settingsScreen())
    }

    fun toSaleScreen() {
        appRouter.navigateTo(Screens.saleScreen())
    }

    fun back() {
        appRouter.exit()
    }
}

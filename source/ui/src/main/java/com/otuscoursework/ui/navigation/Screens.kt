package com.otuscoursework.ui.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.otuscoursework.ui.fragments.auth.AuthFragment
import com.otuscoursework.ui.fragments.cart.CartFragment
import com.otuscoursework.ui.fragments.home.HomeFragment
import com.otuscoursework.ui.fragments.notifications.NotificationFragment
import com.otuscoursework.ui.fragments.orders.OrdersFragment
import com.otuscoursework.ui.fragments.sale.SaleFragment
import com.otuscoursework.ui.fragments.settings.SettingsFragment

object Screens {
    fun authScreen(isCreatePasswordMode: Boolean) = FragmentScreen { AuthFragment.newInstance(isCreatePasswordMode) }
    fun homeScreen() = FragmentScreen { HomeFragment() }
    fun cartScreen() = FragmentScreen { CartFragment() }
    fun ordersScreen(orderId: Int) = FragmentScreen { OrdersFragment.newInstance(orderId) }
    fun notificationsScreen() = FragmentScreen { NotificationFragment() }
    fun saleScreen() = FragmentScreen { SaleFragment() }
    fun settingsScreen() = FragmentScreen { SettingsFragment() }
}
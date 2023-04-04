package com.otuscoursework.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.otuscoursework.ui.fragments.cart.CartFragment
import com.otuscoursework.ui.fragments.home.HomeFragment
import com.otuscoursework.ui.fragments.notifications.NotificationFragment
import com.otuscoursework.ui.fragments.orders.OrdersFragment
import com.otuscoursework.ui.fragments.sale.SaleFragment

object Screens {
    fun homeScreen() = FragmentScreen { HomeFragment() }
    fun cartScreen() = FragmentScreen { CartFragment() }
    fun ordersScreen(orderId: Int) = FragmentScreen { OrdersFragment.newInstance(orderId) }
    fun notificationsScreen() = FragmentScreen { NotificationFragment() }

    fun saleScreen() = FragmentScreen { SaleFragment() }
}
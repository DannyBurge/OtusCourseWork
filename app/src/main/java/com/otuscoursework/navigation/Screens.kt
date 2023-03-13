package com.otuscoursework.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.otuscoursework.ui.fragments.cart.CartFragment
import com.otuscoursework.ui.fragments.home.HomeFragment
import com.otuscoursework.ui.fragments.menuItemDetail.MenuItemDetailFragment
import com.otuscoursework.ui.fragments.notifications.NotificationFragment
import com.otuscoursework.ui.fragments.orders.OrdersFragment
import com.otuscoursework.ui.fragments.sale.SaleFragment
import com.otuscoursework.ui.models.MenuItemUiModel

object Screens {
    fun homeScreen() = FragmentScreen { HomeFragment() }
    fun cartScreen() = FragmentScreen { CartFragment() }
    fun ordersScreen() = FragmentScreen { OrdersFragment() }
    fun notificationsScreen(orderId: Int) =
        FragmentScreen { NotificationFragment.newInstance(orderId) }

    fun saleScreen() = FragmentScreen { SaleFragment() }
}
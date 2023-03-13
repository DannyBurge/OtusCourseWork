package com.otuscoursework.di.components

import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.fragments.cart.CartFragment
import com.otuscoursework.ui.fragments.home.HomeFragment
import com.otuscoursework.ui.fragments.menuItemDetail.MenuItemDetailFragment
import com.otuscoursework.ui.fragments.notifications.NotificationFragment
import com.otuscoursework.ui.fragments.orders.OrdersFragment
import com.otuscoursework.ui.fragments.sale.SaleFragment
import dagger.Component
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
@Component(
    dependencies = [ActivityComponent::class],
)
interface FragmentSaleComponent {
    @Component.Factory
    interface Factory {
        fun create(activityComponent: ActivityComponent): FragmentSaleComponent
    }

    fun provideAppNavigator() : CiceroneAppNavigator

    fun inject(fragment: SaleFragment)
}
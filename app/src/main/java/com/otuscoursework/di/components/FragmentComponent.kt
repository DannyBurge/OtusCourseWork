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
interface FragmentComponent {
    @Component.Factory
    interface Factory {
        fun create(activityComponent: ActivityComponent): FragmentComponent
    }

    fun provideAppNavigator() : CiceroneAppNavigator

    fun provideCartKeeper(): CartKeeper

    fun inject(fragment: HomeFragment)

    fun inject(fragment: MenuItemDetailFragment)

    fun inject(fragment: CartFragment)

    fun inject(fragment: NotificationFragment)

    fun inject(fragment: OrdersFragment)

    fun inject(fragment: SaleFragment)
}
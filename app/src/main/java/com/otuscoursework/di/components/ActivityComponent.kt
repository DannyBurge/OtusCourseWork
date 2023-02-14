package com.otuscoursework.di.components

import com.otuscoursework.di.modules.CartKeeperModule
import com.otuscoursework.di.modules.NavigationModule
import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.main.MainActivity
import dagger.Component
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@Component(
    modules = [NavigationModule::class, CartKeeperModule::class]
)
interface ActivityComponent {

    fun provideCartKeeper(): CartKeeper

    fun provideAppNavigator(): CiceroneAppNavigator

    fun inject(activity: MainActivity)
}
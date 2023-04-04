package com.otuscoursework.di.components

import android.content.Context
import com.otuscoursework.di.modules.CartKeeperModule
import com.otuscoursework.di.modules.NavigationModule
import com.otuscoursework.di.modules.UserDataKeeperModule
import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.main.MainActivity
import dagger.Component
import dagger.hilt.android.scopes.ActivityScoped

@ActivityScoped
@Component(
    modules = [NavigationModule::class, CartKeeperModule::class, UserDataKeeperModule::class]
)
interface ActivityComponent {

    fun provideContext(): Context

    fun provideAppNavigator(): CiceroneAppNavigator

    fun inject(activity: MainActivity)
}
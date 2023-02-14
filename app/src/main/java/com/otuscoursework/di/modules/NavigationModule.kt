package com.otuscoursework.di.modules

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Cicerone.Companion.create
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.otuscoursework.navigation.CiceroneAppNavigator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class NavigationModule {
    private val cicerone: Cicerone<Router> = create()

    @Provides
    @ActivityScoped
    fun provideRouter(): Router {
        return cicerone.router
    }

    @Provides
    @ActivityScoped
    fun provideNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    @Provides
    @ActivityScoped
    fun provideAppNavigator() : CiceroneAppNavigator {
        return CiceroneAppNavigator(cicerone.router)
    }
}
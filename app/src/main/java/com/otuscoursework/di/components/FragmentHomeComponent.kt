package com.otuscoursework.di.components

import com.otuscoursework.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.fragments.home.HomeFragment
import dagger.Component
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
@Component(
    dependencies = [ActivityComponent::class],
)
interface FragmentHomeComponent {
    @Component.Factory
    interface Factory {
        fun create(activityComponent: ActivityComponent): FragmentHomeComponent
    }

    fun provideAppNavigator(): CiceroneAppNavigator

    fun provideCartKeeper(): CartKeeper

    fun inject(fragment: HomeFragment)
}
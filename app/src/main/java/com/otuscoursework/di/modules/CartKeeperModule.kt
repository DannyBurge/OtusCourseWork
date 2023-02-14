package com.otuscoursework.di.modules

import com.otuscoursework.ui.CartKeeper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class CartKeeperModule {

    @Provides
    @ActivityScoped
    fun provideDataKeeper(): CartKeeper = CartKeeper()
}
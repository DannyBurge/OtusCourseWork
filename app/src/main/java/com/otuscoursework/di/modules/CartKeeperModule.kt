package com.otuscoursework.di.modules

import com.otuscoursework.ui.CartKeeper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CartKeeperModule {

    @Provides
    @Singleton
    fun provideDataKeeper(): CartKeeper = CartKeeper()
}
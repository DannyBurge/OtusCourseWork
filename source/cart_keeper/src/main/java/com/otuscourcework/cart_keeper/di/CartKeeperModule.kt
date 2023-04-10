package com.otuscourcework.cart_keeper.di

import com.otuscourcework.cart_keeper.CartKeeper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class CartKeeperModule {

    @Provides
    @Singleton
    fun provideDataKeeper(): CartKeeper = CartKeeper()
}
package com.otuscourcework.user_data_keeper.di

import android.content.Context
import com.otuscourcework.user_data_keeper.Keys
import com.otuscourcework.user_data_keeper.Security
import com.otuscourcework.user_data_keeper.UserDataKeeper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserDataKeeperModule {
    @Provides
    @Singleton
    fun provideKeys(
        context: Context,
    ): Keys {
        return Keys(context.applicationContext)
    }

    @Provides
    @Singleton
    fun provideUserDataKeeper(
        context: Context,
        keys: Keys
    ): UserDataKeeper {
        return UserDataKeeper(context, keys)
    }

    @Provides
    @Singleton
    fun provideSecurity(
        keys: Keys
    ): Security {
        return Security(keys)
    }
}
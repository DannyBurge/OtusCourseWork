package com.otuscourcework.user_data_keeper.di

import android.content.Context
import com.otuscourcework.user_data_keeper.UserDataKeeper
import com.otuscourcework.utils.OtusLogger
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
    fun provideUserDataKeeper(
        otusLogger: OtusLogger,
        context: Context
    ): UserDataKeeper {
        return UserDataKeeper(context, otusLogger)
    }
}
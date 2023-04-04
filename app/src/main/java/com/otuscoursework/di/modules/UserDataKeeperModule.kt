package com.otuscoursework.di.modules

import android.content.Context
import com.otuscoursework.OtusApplication
import com.otuscoursework.ui.UserDataKeeper
import com.otuscoursework.ui.main.MainActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserDataKeeperModule {

    @Provides
    fun provideContext(): Context {
        return OtusApplication.INSTANCE.applicationContext
    }

    @Provides
    @Singleton
    fun provideUserDataKeeper(context: Context): UserDataKeeper {
        return UserDataKeeper(context)
    }
}
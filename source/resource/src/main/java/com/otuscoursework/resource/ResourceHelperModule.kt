package com.otuscoursework.resource

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ResourceHelperModule {

    @Provides
    fun provideHelper(
        context: Context
    ): ResHelper {
        return ResHelper(context)
    }
}
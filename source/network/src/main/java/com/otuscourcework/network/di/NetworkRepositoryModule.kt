package com.otuscourcework.network.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.otuscourcework.network.NetworkApi
import com.otuscourcework.network.NetworkRepository
import com.otuscourcework.network.NetworkRepository.Companion.BASE_URL
import com.otuscourcework.user_data_keeper.Security
import com.otuscourcework.user_data_keeper.UserDataKeeper
import com.otuscourcework.utils.OtusLogger
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkRepositoryModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().build()
    }

    @Provides
    @Singleton
    fun provideChucker(context: Context): ChuckerInterceptor {
        val chuckerCollector = ChuckerCollector(
            context = context,
            showNotification = true,
            retentionPeriod = RetentionManager.Period.ONE_HOUR
        )

        return ChuckerInterceptor
            .Builder(context)
            .collector(chuckerCollector)
            .alwaysReadResponseBody(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiInterceptor(
        userDataKeeper: UserDataKeeper,
        security: Security
    ): Interceptor {
        val apiToken = if (userDataKeeper.apiToken.isNullOrEmpty()) {
            null
        } else {
            security.decryptAes(userDataKeeper.apiToken!!)
        }

        return Interceptor { chain ->
            val url = chain.request()
                .url()
                .newBuilder()
                .addQueryParameter("key", apiToken)
                .build()

            val request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        apiInterceptor: Interceptor,
        chuckerInterceptor: ChuckerInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .addInterceptor(apiInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        baseUrl: String,
        moshi: Moshi,
        client: OkHttpClient
    ): NetworkApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NetworkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLogger(): OtusLogger {
        return OtusLogger().also { it.init() }
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        networkApi: NetworkApi,
        otusLogger: OtusLogger
    ): NetworkRepository {
        return NetworkRepository(networkApi, otusLogger)
    }
}
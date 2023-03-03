package com.otuscoursework.di.modules

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.otuscoursework.BuildConfig
import com.otuscoursework.network.NetworkApi
import com.otuscoursework.network.NetworkRepository
import com.otuscoursework.ui.main.MainActivity
import com.otuscoursework.utils_and_ext.API_KEY
import com.otuscoursework.utils_and_ext.BASE_URL
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
object NetworkApiModule {
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

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key", API_KEY)
                    .build()

                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }

            OkHttpClient
                .Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(
                    ChuckerInterceptor.Builder(MainActivity.INSTANCE.applicationContext).build()
                )
                .build()
        } else {
            OkHttpClient
                .Builder()
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, moshi: Moshi, client: OkHttpClient): NetworkApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NetworkApi::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(networkApi: NetworkApi): NetworkRepository {
        return NetworkRepository(networkApi)
    }
}
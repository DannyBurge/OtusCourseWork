package com.otuscoursework.network

import com.otuscoursework.utils_and_ext.OtusLogger
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val networkApi: NetworkApi
) {

    suspend fun someGetRequest(query: String): String? {
        runCatching {
            delay(2000)
            val result = networkApi.someGetRequest(query).execute()
            return result.body()!!
        }.getOrElse {
            OtusLogger.log(it)
            return null
        }
    }
}
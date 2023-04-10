package com.otuscourcework.network

import androidx.annotation.WorkerThread
import com.otuscourcework.network.models.*
import com.otuscourcework.utils.OtusLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val networkApi: NetworkApi,
    private val otusLogger: OtusLogger
) {
    companion object {
        const val BASE_URL = "http://192.168.31.132:8080/"
    }

    @WorkerThread
    fun getApiKey(): ValidKey? {
        runCatching {
            val result = networkApi.getApiKey().execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun getUserOrders(): List<OrderItem>? {
        runCatching {
            val result = networkApi.getUserOrders().execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun getUserBalanceHistory(isLast: Boolean = false): List<BalanceHistoryItem>? {
        runCatching {
            val result = networkApi.getUserBalanceHistory(isLast).execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun getCategories(): List<CategoryItem>? {
        runCatching {
            val result = networkApi.getCategories().execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun getMenu(idList: List<Int>? = null): List<MenuItem>? {
        runCatching {
            val result = networkApi.getMenu(
                idList?.joinToString(",")
            ).execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun getSales(): Any? {
        runCatching {
            val result = networkApi.getSales().execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun sendOrder(orderItem: OrderItem): OrderItem? {
        runCatching {
            val result = networkApi.sendOrder(orderItem).execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun validateCode(code: String): ValidateCodeItem? {
        runCatching {
            val result = networkApi.validateCode(code).execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun addBalance(balanceHistoryItem: BalanceHistoryItem): BalanceHistoryItem? {
        runCatching {
            val result = networkApi.addBalance(balanceHistoryItem).execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun getUserAddresses(): List<UserDeliveryAddress>? {
        runCatching {
            val result = networkApi.getUserAddresses().execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun sendNewAddress(address: UserDeliveryAddress): UserDeliveryAddress? {
        runCatching {
            val result = networkApi.sendNewAddress(address).execute()
            return result.body()!!
        }.getOrElse {
            otusLogger.log(it)
            return null
        }
    }

    @WorkerThread
    fun updateAddress(address: UserDeliveryAddress): Boolean {
        runCatching {
            val result = networkApi.updateAddress(address).execute()
            return result.isSuccessful
        }.getOrElse {
            otusLogger.log(it)
            return false
        }
    }

    @WorkerThread
    fun deleteAddress(id: Int): Boolean {
        runCatching {
            val result = networkApi.deleteAddress(id).execute()
            return result.isSuccessful
        }.getOrElse {
            otusLogger.log(it)
            return false
        }
    }
}
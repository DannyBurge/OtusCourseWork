package com.otuscoursework.network

import com.otuscoursework.network.models.*
import com.otuscoursework.utils_and_ext.OtusLogger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val networkApi: NetworkApi
) {
    fun getUserOrders(): List<OrderItem>? {
        runCatching {
            val result = networkApi.getUserOrders().execute()
            return result.body()!!
        }.getOrElse {
            OtusLogger.log(it)
            return null
        }
    }

    fun getUserBalanceHistory(isLast: Boolean = false): List<BalanceHistoryItem>? {
        runCatching {
            val result = networkApi.getUserBalanceHistory(isLast).execute()
            return result.body()!!
        }.getOrElse {
            OtusLogger.log(it)
            return null
        }
    }

    fun getCategories(): List<CategoryItem>? {
        runCatching {
            val result = networkApi.getCategories().execute()
            return result.body()!!
        }.getOrElse {
            OtusLogger.log(it)
            return null
        }
    }

    fun getMenu(): List<MenuItem>? {
        runCatching {
            val result = networkApi.getMenu().execute()
            return result.body()!!
        }.getOrElse {
            OtusLogger.log(it)
            return null
        }
    }

    fun getSales(): Any? {
        runCatching {
            val result = networkApi.getSales().execute()
            return result.body()!!
        }.getOrElse {
            OtusLogger.log(it)
            return null
        }
    }

    fun sendOrder(orderItem: OrderItem): Boolean {
        runCatching {
            val result = networkApi.sendOrder(orderItem).execute()
            return result.isSuccessful
        }.getOrElse {
            OtusLogger.log(it)
            return false
        }
    }

    fun sendPromo(code: String): Boolean {
        runCatching {
            val result = networkApi.sendPromo(code).execute()
            return result.isSuccessful
        }.getOrElse {
            OtusLogger.log(it)
            return false
        }
    }

    fun sendNewAddress(address: UserDeliveryAddress): Boolean {
        runCatching {
            val result = networkApi.sendNewAddress(address).execute()
            return result.isSuccessful
        }.getOrElse {
            OtusLogger.log(it)
            return false
        }
    }

    fun updateAddress(address: UserDeliveryAddress): Boolean {
        runCatching {
            val result = networkApi.updateAddress(address).execute()
            return result.isSuccessful
        }.getOrElse {
            OtusLogger.log(it)
            return false
        }
    }

    fun deleteAddress(id: Int): Boolean {
        runCatching {
            val result = networkApi.deleteAddress(id).execute()
            return result.isSuccessful
        }.getOrElse {
            OtusLogger.log(it)
            return false
        }
    }
}
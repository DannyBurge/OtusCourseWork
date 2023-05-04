package com.otuscourcework.network

import com.otuscourcework.network.models.BalanceHistoryItem
import com.otuscourcework.network.models.CategoryItem
import com.otuscourcework.network.models.MenuItem
import com.otuscourcework.network.models.OrderItem
import com.otuscourcework.network.models.UserDeliveryAddress
import com.otuscourcework.network.models.ValidKey
import com.otuscourcework.network.models.ValidateCodeItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface NetworkApi {
    // auth
    @GET("/auth")
    fun getApiKey(): Call<ValidKey>

    // balance
    @GET("/balance/items")
    fun getUserBalanceHistory(
        @Query("isLast") isLast: Boolean
    ): Call<List<BalanceHistoryItem>>

    @POST("/balance/promo")
    fun addBalance(
        @Body balanceHistoryItem: BalanceHistoryItem
    ): Call<BalanceHistoryItem>

    @GET("/balance/validate")
    fun validateCode(
        @Query("code") code: String
    ): Call<ValidateCodeItem>

    // menu
    @GET("/menu/categories")
    fun getCategories(): Call<List<CategoryItem>>

    @GET("/menu/items")
    fun getMenu(
        @Query("id_list") idList: String?
    ): Call<List<MenuItem>>

    // sale
    @GET("/sale/discount")
    fun getSales(): Call<Void>

    // orders
    @GET("/orders")
    fun getUserOrders(): Call<List<OrderItem>>

    @POST("/orders")
    fun sendOrder(
        @Body orderItem: OrderItem
    ): Call<OrderItem>

    // addresses
    @GET("/addresses")
    fun getUserAddresses(): Call<List<UserDeliveryAddress>>

    @POST("/addresses")
    fun sendNewAddress(
        @Body address: UserDeliveryAddress
    ): Call<UserDeliveryAddress>

    @PUT("/addresses")
    fun updateAddress(
        @Body address: UserDeliveryAddress
    ): Call<Void>

    @DELETE("/addresses")
    fun deleteAddress(
        @Query("id") id: Int
    ): Call<Void>
}
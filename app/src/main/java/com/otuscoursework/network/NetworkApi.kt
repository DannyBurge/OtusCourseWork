package com.otuscoursework.network

import com.otuscoursework.network.models.*
import retrofit2.Call
import retrofit2.http.*

interface NetworkApi {

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
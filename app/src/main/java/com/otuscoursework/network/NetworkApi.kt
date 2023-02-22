package com.otuscoursework.network

import com.otuscoursework.network.models.*
import retrofit2.Call
import retrofit2.http.*

interface NetworkApi {
    @GET
    fun getUserOrders(): Call<List<OrderItem>>

    @GET
    fun getUserBalanceHistory(
        @Query("isLast") isLast: Boolean
    ): Call<List<BalanceHistoryItem>>

    @GET
    fun getCategories(): Call<List<CategoryItem>>

    @GET
    fun getMenu(): Call<List<MenuItem>>

    @GET
    fun getSales(): Call<Void>

    @POST
    fun sendOrder(
        @Body orderItem: OrderItem
    ): Call<Void>

    @POST
    fun sendPromo(
        @Query("code") code: String
    ): Call<Void>

    @GET
    fun getUserAddresses(): Call<List<UserDeliveryAddress>>

    @POST
    fun sendNewAddress(
        @Body address: UserDeliveryAddress
    ): Call<Void>

    @PUT
    fun updateAddress(
        @Body address: UserDeliveryAddress
    ): Call<Void>

    @DELETE
    fun deleteAddress(
        @Query("id") id: Int
    ): Call<Void>
}
package com.otuscoursework.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkApi {

    @GET("some_path")
    fun someGetRequest(
        @Query("someQuery") someQuery: String
    ): Call<String>
}
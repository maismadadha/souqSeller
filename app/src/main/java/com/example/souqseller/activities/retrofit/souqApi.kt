package com.example.souqcustomer.retrofit

import com.example.souqseller.activities.pojo.CreateSellerRequest
import com.example.souqseller.activities.pojo.CreateSellerResponse
import com.example.souqseller.activities.pojo.LoginRequest
import com.example.souqseller.activities.pojo.LoginResponse
import com.example.souqseller.activities.pojo.MainCategories
import com.example.souqseller.activities.pojo.OrderResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface souqApi {

    @GET("categories")
    fun getMainCategories(): Call<MainCategories>

    @POST("users")
    fun createUser(
        @Body request: CreateSellerRequest
    ): Call<CreateSellerResponse>

    @POST("seller/login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("orders/store")
    fun getStoreOrders(
        @Query("store_id") storeId: Int,
        @Query("status") status: String?
    ): Call<List<OrderResponse>>


    @GET("order/details")
    fun getOrderDetails(
        @Query("order_id") orderId: Int
    ): Call<OrderResponse>

}